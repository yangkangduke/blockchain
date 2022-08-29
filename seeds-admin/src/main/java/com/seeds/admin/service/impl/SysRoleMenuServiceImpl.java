package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysRoleMenuEntity;
import com.seeds.admin.mapper.SysRoleMenuMapper;
import com.seeds.admin.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统角色菜单
 *
 * @author hang.yu
 * @date 2022/7/14
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuEntity> implements SysRoleMenuService {


    @Override
    public Set<Long> queryMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenuEntity> query = new QueryWrapper<SysRoleMenuEntity>().lambda()
                .eq(SysRoleMenuEntity::getRoleId, roleId);
        List<SysRoleMenuEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream().map(SysRoleMenuEntity::getMenuId).collect(Collectors.toSet());
    }

    @Override
    public List<Long> queryMenuByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysRoleMenuEntity> query = new QueryWrapper<SysRoleMenuEntity>().lambda()
               .in(SysRoleMenuEntity::getRoleId, roleIds);
        List<SysRoleMenuEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(SysRoleMenuEntity::getMenuId).collect(Collectors.toList());
    }

    @Override
    public Map<Long, Set<Long>> queryMenuMapByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<SysRoleMenuEntity> query = new QueryWrapper<SysRoleMenuEntity>().lambda()
                .in(SysRoleMenuEntity::getRoleId, roleIds);
        List<SysRoleMenuEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(SysRoleMenuEntity::getRoleId,
                Collectors.mapping(SysRoleMenuEntity::getMenuId, Collectors.toSet())));
    }

    @Override
    public void deleteByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        LambdaQueryWrapper<SysRoleMenuEntity> query = new QueryWrapper<SysRoleMenuEntity>().lambda()
                .in(SysRoleMenuEntity::getMenuId, menuIds);
        remove(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, Collection<Long> menuIds) {
        // 先删除角色菜单关系
        LambdaQueryWrapper<SysRoleMenuEntity> query = new QueryWrapper<SysRoleMenuEntity>().lambda()
                .eq(SysRoleMenuEntity::getRoleId, roleId);
        remove(query);
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        menuIds.forEach(p -> {
            SysRoleMenuEntity roleMenu = new SysRoleMenuEntity();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(p);
            save(roleMenu);
        });
    }

    @Override
    public void deleteByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        LambdaQueryWrapper<SysRoleMenuEntity> query = new QueryWrapper<SysRoleMenuEntity>().lambda()
               .in(SysRoleMenuEntity::getRoleId, roleIds);
        remove(query);
    }
}