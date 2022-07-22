package com.seeds.admin.service.impl;

import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.service.AdminShiroService;
import com.seeds.admin.service.SysMenuService;
import com.seeds.admin.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 管理后台权限认证
 *
 * @author hang.yu
 * @date 2022/7/14
 */
@Slf4j
@Service
public class AdminShiroServiceImpl implements AdminShiroService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public Set<String> getUserPermissions(Long userId) {

        // 获取用户
        SysUserEntity user = sysUserService.queryById(userId);
        // 系统管理员，拥有最高权限
        List<String> permissionsList;
        if(user.getSuperAdmin() == WhetherEnum.YES.value()) {
            permissionsList = sysMenuService.queryPermissionsList();
        }else{
            permissionsList = sysMenuService.queryUserPermissionsList(user.getId());
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String permissions : permissionsList){
            if(StringUtils.isBlank(permissions)){
                continue;
            }
            permsSet.addAll(Arrays.asList(permissions.trim().split(",")));
        }

        return permsSet;
    }
}