package com.seeds.admin.web.sys.service;


import com.seeds.admin.dto.sys.request.SysUserRoleReq;
import com.seeds.admin.entity.sys.SysRoleUserEntity;

import java.util.Collection;
import java.util.List;

/**
 * 系统角色用户
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public interface SysRoleUserService {

    /**
     * 根据角色id，删除角色用户关系
     * @param roleIds 角色ids
     */
    void deleteByRoleIds(Collection<Long> roleIds);

    /**
     * 根据角色id，查询角色用户关系
     * @param userId 角色ids
     * @return 角色用户关系
     */
    List<SysRoleUserEntity> queryByUserId(Long userId);

    /**
     * 建立用户角色关系
     * @param req 用户角色
     */
    void assignRoles(SysUserRoleReq req);

}
