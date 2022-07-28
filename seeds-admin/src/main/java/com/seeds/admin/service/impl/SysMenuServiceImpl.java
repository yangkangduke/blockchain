package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysMenuAddReq;
import com.seeds.admin.dto.request.SysMenuModifyReq;
import com.seeds.admin.dto.response.SysMenuBriefResp;
import com.seeds.admin.dto.response.SysMenuResp;
import com.seeds.admin.entity.SysMenuEntity;
import com.seeds.admin.entity.SysRoleUserEntity;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.utils.TreeUtils;
import com.seeds.admin.mapper.SysMenuMapper;
import com.seeds.admin.service.SysMenuService;
import com.seeds.admin.service.SysRoleMenuService;
import com.seeds.admin.service.SysRoleUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public void batchDelete(ListReq req) {
        Set<Long> ids = req.getIds();
        // 删除菜单
        removeBatchByIds(ids);
        // 删除角色菜单关系
        sysRoleMenuService.deleteByMenuIds(ids);
    }

    @Override
    public Long countKidsByCodes(Collection<String> codes) {
        LambdaQueryWrapper<SysMenuEntity> query = new QueryWrapper<SysMenuEntity>().lambda()
                .in(SysMenuEntity::getParentCode, codes);
        return count(query);
    }

    @Override
    public List<SysMenuResp> queryRespList(Integer type) {
        return convertToResp(queryList(type));
    }

    @Override
    public List<SysMenuEntity> queryList(Integer type) {
        LambdaQueryWrapper<SysMenuEntity> query = new QueryWrapper<SysMenuEntity>().lambda()
                .eq(type != null,SysMenuEntity::getType, type)
                .orderByAsc(SysMenuEntity::getSort);
        return list(query);
    }

    @Override
    public SysMenuResp detail(Long id) {
        SysMenuEntity menu = getById(id);
        SysMenuResp resp = new SysMenuResp();
        if (menu != null) {
            BeanUtils.copyProperties(menu, resp);
            SysMenuEntity parent = queryByMenuCode(menu.getParentCode());
            if (parent != null) {
                resp.setParentName(parent.getName());
            }
        }
        return resp;
    }

    @Override
    public Set<String> queryCodesByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptySet();
        }
        LambdaQueryWrapper<SysMenuEntity> query = new QueryWrapper<SysMenuEntity>().lambda()
                .in(SysMenuEntity::getId, ids);
        List<SysMenuEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream().map(SysMenuEntity::getCode).collect(Collectors.toSet());
    }

    @Override
    public SysMenuEntity queryByMenuCode(String code) {
        LambdaQueryWrapper<SysMenuEntity> query = new QueryWrapper<SysMenuEntity>().lambda()
                .eq(SysMenuEntity::getCode, code);
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
    public List<String> queryPermissionsList() {
        List<SysMenuEntity> list = list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(SysMenuEntity::getPermissions).collect(Collectors.toList());
    }

    @Override
    public List<String> queryUserPermissionsList(Long userId) {
        List<SysMenuEntity> menu = queryMenuListByUserId(userId);
        if (CollectionUtils.isEmpty(menu)) {
            return Collections.emptyList();
        }
        // 返回权限
        return menu.stream().map(SysMenuEntity::getPermissions).collect(Collectors.toList());
    }

    @Override
    public List<SysMenuEntity> queryMenuListByUserId(Long userId) {
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
        return listByIds(menuIds);
    }

    @Override
    public List<SysMenuBriefResp> getUserMenuList(SysUserEntity user) {
        List<SysMenuEntity> menuList;
        // 系统管理员，拥有最高权限
        if (user.getSuperAdmin() == WhetherEnum.YES.value()) {
            menuList = queryList(null);
        } else {
            menuList = queryMenuListByUserId(user.getId());
        }
        if (CollectionUtils.isEmpty(menuList)) {
            return Collections.emptyList();
        }
        List<SysMenuBriefResp> respList = new ArrayList<>();
        menuList.forEach(p -> {
            SysMenuBriefResp resp = new SysMenuBriefResp();
            BeanUtils.copyProperties(p, resp);
            respList.add(resp);
        });
        return respList;
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
        // 菜单根节点为空
        return TreeUtils.build(respList, "");
    }
}