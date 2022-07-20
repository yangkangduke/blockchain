package com.seeds.admin.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.sys.request.SysUserRoleReq;
import com.seeds.admin.entity.sys.SysRoleUserEntity;
import com.seeds.admin.web.sys.mapper.SysRoleUserMapper;
import com.seeds.admin.web.sys.service.SysRoleUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统角色用户
 *
 * @author hang.yu
 * @date 2022/7/14
 */
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUserEntity> implements SysRoleUserService {

    @Override
    public void deleteByRoleIds(Collection<Long> roleIds) {
        QueryWrapper<SysRoleUserEntity> query = new QueryWrapper<>();
        query.in("role_id", roleIds);
        remove(query);
    }

    @Override
    public List<SysRoleUserEntity> queryByUserId(Long userId) {
        QueryWrapper<SysRoleUserEntity> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        return list(query);
    }

    @Override
    public Map<Long, Set<Long>> queryMapByUserIds(Collection<Long> userIds) {
        QueryWrapper<SysRoleUserEntity> query = new QueryWrapper<>();
        query.in("user_id", userIds);
        List<SysRoleUserEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(SysRoleUserEntity::getUserId,
                Collectors.mapping(SysRoleUserEntity::getRoleId, Collectors.toSet())));
    }

    @Override
    public void assignRoles(SysUserRoleReq req) {
        // 排除已存在用户角色关系
        List<SysRoleUserEntity> roleUserList = queryByUserId(req.getUserId());
        Set<Long> roleIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(roleUserList)) {
            roleIdSet = roleUserList.stream().map(SysRoleUserEntity::getRoleId)
                    .filter(p -> !req.getRoleIds().contains(p)).collect(Collectors.toSet());
        }
        if (CollectionUtils.isEmpty(roleIdSet)) {
            return;
        }
        // 建立用户角色关系
        for (Long roleId : roleIdSet) {
            SysRoleUserEntity roleUser = new SysRoleUserEntity();
            roleUser.setUserId(req.getUserId());
            roleUser.setRoleId(roleId);
            save(roleUser);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoles(SysUserRoleReq req) {
        List<Long> roleIds = req.getRoleIds();
        // 先删除角色用户关系
        deleteByUserIds(Collections.singletonList(req.getUserId()));
        //用户没有一个角色权限的情况
        if(CollectionUtils.isEmpty(roleIds)){
            return;
        }
        // 建立用户角色关系
        for (Long roleId : roleIds) {
            SysRoleUserEntity roleUser = new SysRoleUserEntity();
            roleUser.setUserId(req.getUserId());
            roleUser.setRoleId(roleId);
            save(roleUser);
        }
    }

    @Override
    public void deleteByUserIds(Collection<Long> userIds) {
        QueryWrapper<SysRoleUserEntity> query = new QueryWrapper<>();
        query.in("user_id", userIds);
        remove(query);
    }
}