package com.seeds.admin.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.auth.response.AdminUserResp;
import com.seeds.admin.dto.common.SwitchReq;
import com.seeds.admin.dto.sys.request.SysUserAddReq;
import com.seeds.admin.dto.sys.request.SysUserModifyReq;
import com.seeds.admin.dto.sys.request.SysUserPageReq;
import com.seeds.admin.dto.sys.response.SysUserBriefResp;
import com.seeds.admin.dto.sys.response.SysUserResp;
import com.seeds.admin.entity.sys.SysRoleEntity;
import com.seeds.admin.entity.sys.SysRoleUserEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.web.sys.mapper.SysUserMapper;
import com.seeds.admin.entity.sys.SysUserEntity;
import com.seeds.admin.web.sys.service.SysMenuService;
import com.seeds.admin.web.sys.service.SysRoleService;
import com.seeds.admin.web.sys.service.SysRoleUserService;
import com.seeds.admin.web.sys.service.SysUserService;
import com.seeds.admin.utils.HashUtil;
import com.seeds.admin.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author hang.yu
 * @date 2022/7/13
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Override
    public SysUserEntity queryByMobile(String mobile) {
        QueryWrapper<SysUserEntity> query = new QueryWrapper<>();
        query.eq("mobile", mobile);
        query.eq("delete_flag", WhetherEnum.NO.value());
        return getOne(query);
    }

    @Override
    public SysUserEntity queryByAccount(String account) {
        QueryWrapper<SysUserEntity> query = new QueryWrapper<>();
        query.eq("account", account);
        query.eq("delete_flag", WhetherEnum.NO.value());
        return getOne(query);
    }

    @Override
    public SysUserEntity queryById(Long userId) {
        return getById(userId);
    }

    @Override
    public IPage<SysUserResp> queryPage(SysUserPageReq query) {
        QueryWrapper<SysUserEntity> queryWrap = new QueryWrapper<>();
        queryWrap.likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), "real_name", query.getNameOrMobile())
                .or().likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), "mobile", query.getNameOrMobile());
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        Page<SysUserEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysUserEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> userIds = records.stream().map(SysUserEntity::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> roleUserMap = sysRoleUserService.queryMapByUserIds(userIds);
        Set<Long> roleIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(roleUserMap.values())) {
            roleUserMap.values().forEach(roleIds::addAll);
        }
        Map<Long, String> roleMap = sysRoleService.queryMapByIds(roleIds);
        return page.convert(p -> {
            SysUserResp resp = new SysUserResp();
            BeanUtils.copyProperties(p, resp);
            StringBuilder str = new StringBuilder();
            Set<Long> roleIdSet = roleUserMap.get(p.getId());
            if (!CollectionUtils.isEmpty(roleIdSet)) {
                resp.setRoleIds(new ArrayList<>(roleIdSet));
                for (Long roleId : roleIdSet) {
                    String roleName = roleMap.get(roleId);
                    if (StringUtils.isEmpty(roleName)) {
                        continue;
                    }
                    str.append(roleName).append(",");
                }
            }
            if (str.length() > 0) {
                resp.setRoleNameStr(str.substring(0, str.lastIndexOf(",")));
            }
            return resp;
        });
    }

    @Override
    public SysUserEntity add(SysUserAddReq user) {
        SysUserEntity adminUser = new SysUserEntity();
        BeanUtils.copyProperties(user, adminUser);
        String salt = RandomUtil.getRandomSalt();
        String password = HashUtil.sha256(user.getInitPassport() + salt);
        adminUser.setPassword(password);
        adminUser.setSalt(salt);
        adminUser.setSuperAdmin(WhetherEnum.NO.value());
        adminUser.setStatus(SysStatusEnum.ENABLED.value());
        save(adminUser);
        return adminUser;
    }

    @Override
    public SysUserEntity modify(SysUserModifyReq user) {
        SysUserEntity adminUser = new SysUserEntity();
        BeanUtils.copyProperties(user, adminUser);
        updateById(adminUser);
        return adminUser;
    }

    @Override
    public void modifyById(SysUserEntity sysUser) {
        updateById(sysUser);
    }

    @Override
    public void batchModifyById(List<SysUserEntity> sysUsers) {
        updateBatchById(sysUsers);
    }

    @Override
    public List<SysUserEntity> queryByIds(Collection<Long> ids) {
        QueryWrapper<SysUserEntity> queryWrap = new QueryWrapper<>();
        queryWrap.in("id", ids);
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        return list(queryWrap);
    }

    @Override
    public Map<Long, String> queryNameMapByIds(Collection<Long> ids) {
        List<SysUserEntity> sysUser = queryByIds(ids);
        if (CollectionUtils.isEmpty(sysUser)) {
            return Collections.emptyMap();
        }
        return sysUser.stream().collect(Collectors.toMap(SysUserEntity::getId, SysUserEntity::getRealName));
    }

    @Override
    public void batchDelete(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<SysUserEntity> list = queryByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(p -> p.setDeleteFlag(WhetherEnum.YES.value()));
        updateBatchById(list);
    }

    @Override
    public void updatePassword(SysUserEntity adminUser, String newPassword) {
        String salt = RandomUtil.getRandomSalt();
        String password = HashUtil.sha256(newPassword + salt);
        adminUser.setSalt(salt);
        adminUser.setPassword(password);
        updateById(adminUser);
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {
        if (CollectionUtils.isEmpty(req)) {
            return;
        }
        List<SysUserEntity> sysUsers = new ArrayList<>();
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysUserEntity sysUser = new SysUserEntity();
            sysUser.setId(p.getId());
            sysUser.setStatus(p.getStatus());
            sysUsers.add(sysUser);
        });
        // 停用/启用用户
        updateBatchById(sysUsers);
    }

    @Override
    public AdminUserResp queryLoginUserInfo(Long userId) {
        SysUserEntity user = queryById(userId);
        AdminUserResp resp = new AdminUserResp();
        if (user != null) {
            // 用户信息
            BeanUtils.copyProperties(user, resp);
            // 角色信息 菜单信息
            if (WhetherEnum.YES.value() == user.getSuperAdmin()) {
                // 超级管理员
                resp.setRoleList(sysRoleService.queryList());
                resp.setMenuList(sysMenuService.queryList(null));
            } else {
                resp.setRoleList(sysRoleService.queryByUserId(userId));
                resp.setMenuList(sysMenuService.queryByUserId(null, userId));
            }
        }
        return resp;
    }

    @Override
    public SysUserResp detail(Long id) {
        SysUserEntity user = queryById(id);
        SysUserResp resp = new SysUserResp();
        if (user != null) {
            BeanUtils.copyProperties(user, resp);
            List<SysRoleUserEntity> sysRoleUser = sysRoleUserService.queryByUserId(user.getId());
            if (!CollectionUtils.isEmpty(sysRoleUser)) {
                StringBuilder str = new StringBuilder();
                Set<Long> roleIds = sysRoleUser.stream().map(SysRoleUserEntity::getRoleId).collect(Collectors.toSet());
                resp.setRoleIds(new ArrayList<>(roleIds));
                List<SysRoleEntity> sysRole = sysRoleService.queryByIds(roleIds);
                sysRole.forEach(p -> str.append(p.getRoleName()).append(","));
                if (str.length() > 0) {
                    resp.setRoleNameStr(str.substring(0, str.lastIndexOf(",")));
                }
            }
        }
        return resp;
    }

    @Override
    public SysUserBriefResp brief(String mobile) {
        QueryWrapper<SysUserEntity> queryWrap = new QueryWrapper<>();
        queryWrap.eq("mobile", mobile);
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        SysUserEntity sysUser = getOne(queryWrap);
        SysUserBriefResp res = new SysUserBriefResp();
        if (sysUser != null) {
            res.setMobile(mobile);
            res.setId(sysUser.getId());
            res.setRealName(sysUser.getRealName());
        }
        return res;
    }

}