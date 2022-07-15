package com.seeds.admin.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.sys.request.SysMenuAddReq;
import com.seeds.admin.dto.sys.request.SysMenuModifyReq;
import com.seeds.admin.dto.sys.response.SysMenuResp;
import com.seeds.admin.entity.sys.SysMenuEntity;
import com.seeds.admin.entity.sys.SysRoleUserEntity;
import com.seeds.admin.utils.TreeUtils;
import com.seeds.admin.web.sys.mapper.SysMenuMapper;
import com.seeds.admin.web.sys.service.SysMenuService;
import com.seeds.admin.web.sys.service.SysRoleMenuService;
import com.seeds.admin.web.sys.service.SysRoleUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 系统用户
 *
 * @author hang.yu
 * @date 2022/7/13
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuEntity> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Override
    public void add(SysMenuAddReq req) {
        SysMenuEntity menu = new SysMenuEntity();
        BeanUtils.copyProperties(req, menu);
        save(menu);
    }

    @Override
    public void modify(SysMenuModifyReq req) {
        SysMenuEntity menu = new SysMenuEntity();
        BeanUtils.copyProperties(req, menu);
        updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 删除菜单
        removeById(id);
        // 删除角色菜单关系
        sysRoleMenuService.deleteByMenuId(id);
    }

    @Override
    public List<SysMenuEntity> queryListByPid(Long id) {
        QueryWrapper<SysMenuEntity> query = new QueryWrapper<>();
        query.eq("p_id", id);
        query.orderByAsc("sort");
        return list(query);
    }

    @Override
    public List<SysMenuResp> queryList(Integer type) {
        QueryWrapper<SysMenuEntity> query = new QueryWrapper<>();
        query.eq(type != null,"type", type);
        query.orderByAsc("sort");
        List<SysMenuEntity> list = list(query);
        return convertToResp(list);
    }

    @Override
    public SysMenuResp detail(Long id) {
        SysMenuEntity menu = getById(id);
        SysMenuResp resp = new SysMenuResp();
        if (menu != null) {
            BeanUtils.copyProperties(menu, resp);
            SysMenuEntity parent = getById(menu.getPid());
            if (parent != null) {
                resp.setParentName(parent.getName());
            }
        }
        return resp;
    }

    @Override
    public SysMenuEntity queryByMenuCode(String code) {
        QueryWrapper<SysMenuEntity> query = new QueryWrapper<>();
        query.eq("code", code);
        return getOne(query);
    }

    @Override
    public List<SysMenuResp> queryByUserId(Integer type, Long userId) {
        List<SysRoleUserEntity> roleUsers = sysRoleUserService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(roleUsers)) {
            return Collections.emptyList();
        }
        Set<Long> roleIds = roleUsers.stream().map(SysRoleUserEntity::getRoleId).collect(Collectors.toSet());
        List<Long> menuIds = sysRoleMenuService.queryMenuByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        List<SysMenuEntity> list = listByIds(menuIds);
        return convertToResp(list);
    }

    @Override
    public List<String> getPermissionsList() {
        List<SysMenuEntity> list = list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(SysMenuEntity::getPermissions).collect(Collectors.toList());
    }

    @Override
    public List<String> getUserPermissionsList(Long userId) {
        // 查询角色用户关联
        List<SysRoleUserEntity> roleUser = sysRoleUserService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(roleUser)) {
            return Collections.emptyList();
        }
        // 查询角色菜单关联
        List<Long> roleIds = roleUser.stream().map(SysRoleUserEntity::getRoleId).collect(Collectors.toList());
        List<Long> menuIds = sysRoleMenuService.queryMenuByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        // 查询菜单
        List<SysMenuEntity> menu = listByIds(menuIds);
        if (CollectionUtils.isEmpty(menu)) {
            return Collections.emptyList();
        }
        // 返回权限
        return menu.stream().map(SysMenuEntity::getPermissions).collect(Collectors.toList());
    }

    private List<SysMenuResp> convertToResp(List<SysMenuEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SysMenuResp> respList = new ArrayList<>();
        for (SysMenuEntity menu : list) {
            SysMenuResp resp = new SysMenuResp();
            BeanUtils.copyProperties(menu, resp);
            respList.add(resp);
        }
        // 菜单根节点为0
        return TreeUtils.build(respList, 0L);
    }
}