package com.seeds.admin.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.auth.response.AdminUserResp;
import com.seeds.admin.dto.sys.request.SysUserAddReq;
import com.seeds.admin.dto.sys.request.SysUserModifyReq;
import com.seeds.admin.dto.sys.request.SysUserPageReq;
import com.seeds.admin.dto.sys.response.SysUserResp;
import com.seeds.admin.enums.SuperAdminEnum;
import com.seeds.admin.web.sys.mapper.SysUserMapper;
import com.seeds.admin.entity.sys.SysUserEntity;
import com.seeds.admin.web.sys.service.SysMenuService;
import com.seeds.admin.web.sys.service.SysRoleService;
import com.seeds.admin.web.sys.service.SysUserService;
import com.seeds.admin.utils.HashUtil;
import com.seeds.admin.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

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

    @Override
    public SysUserEntity queryByMobile(String mobile) {
        QueryWrapper<SysUserEntity> query = new QueryWrapper<>();
        query.eq("mobile", mobile);
        query.eq("delete_flag", 0);
        return getOne(query);
    }

    @Override
    public SysUserEntity queryByAccount(String account) {
        QueryWrapper<SysUserEntity> query = new QueryWrapper<>();
        query.eq("account", account);
        query.eq("delete_flag", 0);
        return getOne(query);
    }

    @Override
    public SysUserEntity queryById(Long userId) {
        return getById(userId);
    }

    @Override
    public IPage<SysUserResp> queryPage(SysUserPageReq query) {
        QueryWrapper<SysUserEntity> queryWrap = new QueryWrapper<>();
        queryWrap.eq(StringUtils.isNotBlank(query.getNameOrMobile()), "real_name", query.getNameOrMobile()).or().eq(StringUtils.isNotBlank(query.getNameOrMobile()), "mobile", query.getNameOrMobile());
        queryWrap.eq("delete_flag", 0);
        Page<SysUserEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysUserEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysUserResp resp = new SysUserResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public void add(SysUserAddReq user) {
        SysUserEntity adminUser = new SysUserEntity();
        BeanUtils.copyProperties(user, adminUser);
        String salt = RandomUtil.getRandomSalt();
        String password = HashUtil.sha256(user.getInitPassport() + salt);
        adminUser.setPassword(password);
        adminUser.setSalt(salt);
        adminUser.setSuperAdmin(0);
        adminUser.setStatus(1);
        save(adminUser);
    }

    @Override
    public void modify(SysUserModifyReq user) {
        SysUserEntity adminUser = new SysUserEntity();
        BeanUtils.copyProperties(user, adminUser);
        updateById(adminUser);
    }

    @Override
    public List<SysUserEntity> queryByIds(Set<Long> ids) {
        QueryWrapper<SysUserEntity> queryWrap = new QueryWrapper<>();
        queryWrap.in("id", ids);
        queryWrap.eq("delete_flag", 0);
        return list(queryWrap);
    }

    @Override
    public void batchDelete(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<SysUserEntity> list = queryByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(p -> p.setDeleteFlag(1));
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
    public void enableOrDisable(Set<Long> ids, Integer status) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<SysUserEntity> list = queryByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(p -> p.setStatus(status));
        updateBatchById(list);
    }

    @Override
    public AdminUserResp queryLoginUserInfo(Long userId) {
        SysUserEntity user = queryById(userId);
        AdminUserResp resp = new AdminUserResp();
        if (user != null) {
            // 用户信息
            BeanUtils.copyProperties(user, resp);
            // 角色信息 菜单信息
            if (SuperAdminEnum.YES.value() == user.getSuperAdmin()) {
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
        }
        return resp;
    }

}