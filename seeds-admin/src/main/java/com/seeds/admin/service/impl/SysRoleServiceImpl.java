package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SysRoleAddReq;
import com.seeds.admin.dto.request.SysRoleAssignReq;
import com.seeds.admin.dto.request.SysRoleModifyReq;
import com.seeds.admin.dto.request.SysRolePageReq;
import com.seeds.admin.dto.response.SysRoleResp;
import com.seeds.admin.entity.SysRoleEntity;
import com.seeds.admin.entity.SysRoleUserEntity;
import com.seeds.admin.mapper.SysRoleMapper;
import com.seeds.admin.service.SysRoleMenuService;
import com.seeds.admin.service.SysRoleService;
import com.seeds.admin.service.SysRoleUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统角色
 *
 * @author hang.yu
 * @date 2022/7/14
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Override
    public IPage<SysRoleResp> queryPage(SysRolePageReq query) {
        LambdaQueryWrapper<SysRoleEntity> queryWrap = new QueryWrapper<SysRoleEntity>().lambda()
                .eq(StringUtils.isNotBlank(query.getRoleName()), SysRoleEntity::getRoleName, query.getRoleName())
                .orderByDesc(SysRoleEntity::getCreatedAt);
        Page<SysRoleEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysRoleEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        // 查询角色对应的菜单
        Set<Long> roleIds = records.stream().map(SysRoleEntity::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> menuMap = sysRoleMenuService.queryMenuMapByRoleIds(roleIds);
        return page.convert(p -> {
            SysRoleResp resp = new SysRoleResp();
            BeanUtils.copyProperties(p, resp);
            resp.setMenuIdList(menuMap.get(p.getId()));
            return resp;
        });
    }

    @Override
    public SysRoleEntity queryByRoleCode(String roleCode) {
        LambdaQueryWrapper<SysRoleEntity> query = new QueryWrapper<SysRoleEntity>().lambda()
                .eq(SysRoleEntity::getRoleCode, roleCode);
        return getOne(query);
    }

    @Override
    public Map<Long, String> queryMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<SysRoleEntity> list = listByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(SysRoleEntity::getId, SysRoleEntity::getRoleName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysRoleAddReq req) {
        // 保存角色
        SysRoleEntity role = new SysRoleEntity();
        BeanUtils.copyProperties(req, role);
        save(role);
        // 保存角色菜单关系
        sysRoleMenuService.saveOrUpdate(role.getId(), req.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(SysRoleModifyReq req) {
        // 更新角色
        SysRoleEntity role = new SysRoleEntity();
        BeanUtils.copyProperties(req, role);
        updateById(role);
        // 更新角色菜单关系
        sysRoleMenuService.saveOrUpdate(role.getId(), req.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Collection<Long> ids) {
        // 删除角色
        removeByIds(ids);
        // 删除角色用户关系
        sysRoleUserService.deleteByRoleIds(ids);
        // 删除角色菜单关系
        sysRoleMenuService.deleteByRoleIds(ids);
    }

    @Override
    public SysRoleResp detail(Long id) {
        SysRoleEntity role = getById(id);
        SysRoleResp resp = new SysRoleResp();
        if (role != null) {
            BeanUtils.copyProperties(role, resp);
            // 查询角色对应的菜单
            resp.setMenuIdList(sysRoleMenuService.queryMenuByRoleId(role.getId()));
        }
        return resp;
    }

    @Override
    public List<SysRoleResp> queryList() {
        return convertToResp(list());
    }

    @Override
    public List<SysRoleResp> queryByUserId(Long userId) {
        List<SysRoleUserEntity> roleUser = sysRoleUserService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(roleUser)) {
            return Collections.emptyList();
        }
        Set<Long> roleIds = roleUser.stream().map(SysRoleUserEntity::getRoleId).collect(Collectors.toSet());
        List<SysRoleEntity> list = listByIds(roleIds);
        return convertToResp(list);
    }

    @Override
    public void assign(SysRoleAssignReq req) {
        List<Long> userIds = req.getUserIds();
        List<SysRoleUserEntity> roleUsers = sysRoleUserService.queryByRoleId(req.getRoleId());
        Set<Long> userIdSet;
        if (CollectionUtils.isEmpty(roleUsers)) {
            userIdSet = new HashSet<>(userIds);
        } else {
            // 排除已存在用户角色关系
            Set<Long> existUsers = roleUsers.stream().map(SysRoleUserEntity::getUserId).collect(Collectors.toSet());
            userIdSet = userIds.stream().filter(p -> !existUsers.contains(p)).collect(Collectors.toSet());
        }
        if (CollectionUtils.isEmpty(userIdSet)) {
            return;
        }
        userIdSet.forEach(p -> {
            SysRoleUserEntity roleUser = new SysRoleUserEntity();
            roleUser.setRoleId(req.getRoleId());
            roleUser.setUserId(p);
            sysRoleUserService.save(roleUser);
        });
    }

    private List<SysRoleResp> convertToResp(List<SysRoleEntity> list){
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SysRoleResp> respList = new ArrayList<>();
        list.forEach(p -> {
            SysRoleResp resp = new SysRoleResp();
            BeanUtils.copyProperties(p, resp);
            respList.add(resp);
        });
        return respList;
    }
}