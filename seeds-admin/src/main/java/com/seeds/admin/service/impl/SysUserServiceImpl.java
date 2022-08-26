package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysUserAddReq;
import com.seeds.admin.dto.request.SysUserModifyReq;
import com.seeds.admin.dto.request.SysUserPageReq;
import com.seeds.admin.dto.response.AdminUserResp;
import com.seeds.admin.dto.response.SysUserBriefResp;
import com.seeds.admin.dto.response.SysUserResp;
import com.seeds.admin.entity.SysRoleEntity;
import com.seeds.admin.entity.SysRoleUserEntity;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.MetaMaskFlagEnum;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mapper.SysUserMapper;
import com.seeds.admin.service.*;
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
        LambdaQueryWrapper<SysUserEntity> query = new QueryWrapper<SysUserEntity>().lambda()
                .eq(SysUserEntity::getMobile, mobile);
        return getOne(query);
    }

    @Override
    public SysUserEntity queryByAccount(String account) {
        LambdaQueryWrapper<SysUserEntity> query = new QueryWrapper<SysUserEntity>().lambda()
                .eq(SysUserEntity::getAccount, account);
        return getOne(query);
    }

    @Override
    public IPage<SysUserResp> queryPage(SysUserPageReq query) {
        LambdaQueryWrapper<SysUserEntity> queryWrap = new QueryWrapper<SysUserEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), SysUserEntity::getRealName, query.getNameOrMobile())
                .or().likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), SysUserEntity::getMobile, query.getNameOrMobile())
                .eq(query.getDeptId() != null, SysUserEntity::getDeptId, query.getDeptId())
                .orderByDesc(SysUserEntity::getCreatedAt);
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
    public Map<Long, String> queryNameMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<SysUserEntity> sysUser = listByIds(ids);
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
        removeByIds(ids);
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
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysUserEntity sysUser = new SysUserEntity();
            sysUser.setId(p.getId());
            sysUser.setStatus(p.getStatus());
            // 停用/启用用户
            updateById(sysUser);
        });
    }

    @Override
    public AdminUserResp queryLoginUserInfo(Long userId) {
        SysUserEntity user = getById(userId);
        AdminUserResp resp = new AdminUserResp();
        if (user != null) {
            // 用户信息
            BeanUtils.copyProperties(user, resp);
            // 角色信息 菜单信息
            if (WhetherEnum.YES.value() == user.getSuperAdmin()) {
                // 超级管理员
                resp.setRoleList(sysRoleService.queryList());
                resp.setMenuList(sysMenuService.queryShowMenu());
            } else {
                resp.setRoleList(sysRoleService.queryByUserId(userId));
                resp.setMenuList(sysMenuService.queryByUserId(userId));
            }
        }
        return resp;
    }

    @Override
    public SysUserResp detail(Long id) {
        SysUserEntity user = getById(id);
        SysUserResp resp = new SysUserResp();
        if (user != null) {
            BeanUtils.copyProperties(user, resp);
            List<SysRoleUserEntity> sysRoleUser = sysRoleUserService.queryByUserId(user.getId());
            if (!CollectionUtils.isEmpty(sysRoleUser)) {
                StringBuilder str = new StringBuilder();
                Set<Long> roleIds = sysRoleUser.stream().map(SysRoleUserEntity::getRoleId).collect(Collectors.toSet());
                resp.setRoleIds(new ArrayList<>(roleIds));
                List<SysRoleEntity> sysRole = sysRoleService.listByIds(roleIds);
                sysRole.forEach(p -> str.append(p.getRoleName()).append(","));
                if (str.length() > 0) {
                    resp.setRoleNameStr(str.substring(0, str.lastIndexOf(",")));
                }
            }
            // 头像
            resp.setHeadUrl(user.getUrl());
        }
        return resp;
    }

    @Override
    public SysUserBriefResp brief(String mobile) {
        LambdaQueryWrapper<SysUserEntity> queryWrap = new QueryWrapper<SysUserEntity>().lambda()
                .eq(SysUserEntity::getMobile, mobile);
        SysUserEntity sysUser = getOne(queryWrap);
        SysUserBriefResp res = new SysUserBriefResp();
        if (sysUser != null) {
            res.setMobile(mobile);
            res.setId(sysUser.getId());
            res.setRealName(sysUser.getRealName());
        }
        return res;
    }

    /**
     * metamask获取随机数
     *
     * @param publicAddress
     * @return
     */
    @Override
    public String metamaskNonce(String publicAddress, Long userId) {
        // 生成随机数
        String randomSalt = RandomUtil.getRandomSalt();
        SysUserEntity sysUser = new SysUserEntity();
        sysUser.setPublicAddress(publicAddress);
        sysUser.setNonce(randomSalt);
        sysUser.setId(userId);
        this.updateById(sysUser);
        return randomSalt;
    }

    /**
     * 更新metamask信息
     *
     */
    @Override
    public Boolean updateMetaMask(Long userId) {
        SysUserEntity sysUser = new SysUserEntity();
        sysUser.setNonce(RandomUtil.getRandomSalt());
        sysUser.setMetamaskFlag(MetaMaskFlagEnum.ENABLED.value());
        return this.update(sysUser, new QueryWrapper<SysUserEntity>().lambda()
                .eq(SysUserEntity::getId, userId));
    }

    /**
     * 删除metaemask相关信息
     * @param userId
     * @return
     */
    @Override
    public Boolean deleteMetaMask(Long userId) {
        return this.update(
                Wrappers.<SysUserEntity>lambdaUpdate()
                        .set(SysUserEntity::getPublicAddress, null)
                        .set(SysUserEntity::getNonce, null)
                        .set(SysUserEntity::getMetamaskFlag, MetaMaskFlagEnum.DISABLE.value())
        );
    }

    @Override
    public IPage<SysUserBriefResp> dropdownPage(SysUserPageReq query) {
        LambdaQueryWrapper<SysUserEntity> queryWrap = new QueryWrapper<SysUserEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), SysUserEntity::getRealName, query.getNameOrMobile())
                .or().likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), SysUserEntity::getMobile, query.getNameOrMobile());
        Page<SysUserEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysUserEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysUserBriefResp resp = new SysUserBriefResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

}