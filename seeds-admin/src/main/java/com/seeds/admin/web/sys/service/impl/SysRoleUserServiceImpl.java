package com.seeds.admin.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.sys.request.SysUserRoleReq;
import com.seeds.admin.entity.sys.SysRoleUserEntity;
import com.seeds.admin.web.sys.mapper.SysRoleUserMapper;
import com.seeds.admin.web.sys.service.SysRoleUserService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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
    public void assignRoles(SysUserRoleReq req) {
        // 建立用户角色关系
        SysRoleUserEntity roleUser = new SysRoleUserEntity();
        roleUser.setUserId(req.getUserId());
        roleUser.setRoleId(req.getRoleId());
        save(roleUser);
    }
}