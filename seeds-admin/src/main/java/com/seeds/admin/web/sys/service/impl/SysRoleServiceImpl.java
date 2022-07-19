package com.seeds.admin.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.sys.request.SysRoleAddReq;
import com.seeds.admin.dto.sys.request.SysRoleModifyReq;
import com.seeds.admin.dto.sys.request.SysRolePageReq;
import com.seeds.admin.dto.sys.response.SysRoleResp;
import com.seeds.admin.entity.sys.SysRoleEntity;
import com.seeds.admin.entity.sys.SysRoleUserEntity;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.web.sys.mapper.SysRoleMapper;
import com.seeds.admin.web.sys.service.SysRoleMenuService;
import com.seeds.admin.web.sys.service.SysRoleService;
import com.seeds.admin.web.sys.service.SysRoleUserService;
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
        QueryWrapper<SysRoleEntity> queryWrap = new QueryWrapper<>();
        queryWrap.eq(StringUtils.isNotBlank(query.getRoleName()), "roleName", query.getRoleName());
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        Page<SysRoleEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysRoleEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysRoleResp resp = new SysRoleResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public SysRoleEntity queryByRoleCode(String roleCode) {
        QueryWrapper<SysRoleEntity> query = new QueryWrapper<>();
        query.eq("role_code", roleCode);
        query.eq("delete_flag", WhetherEnum.NO.value());
        return getOne(query);
    }

    @Override
    public SysRoleEntity queryById(Long id) {
        QueryWrapper<SysRoleEntity> query = new QueryWrapper<>();
        query.eq("id", id);
        query.eq("delete_flag", WhetherEnum.NO.value());
        return getOne(query);
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
    public void batchDelete(Set<Long> ids) {
        // 删除角色
        removeByIds(ids);
        // 删除角色用户关系
        sysRoleUserService.deleteByRoleIds(ids);
        // 删除角色菜单关系
        sysRoleMenuService.deleteByRoleIds(ids);
    }

    @Override
    public SysRoleResp detail(Long id) {
        SysRoleEntity role = queryById(id);
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
        QueryWrapper<SysRoleEntity> query = new QueryWrapper<>();
        query.eq("delete_flag", WhetherEnum.NO.value());
        List<SysRoleEntity> list = list(query);
        return convertToResp(list);
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