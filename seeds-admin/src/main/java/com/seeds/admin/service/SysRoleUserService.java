package com.seeds.admin.service;


import com.seeds.admin.dto.request.SysUserRoleReq;
import com.seeds.admin.entity.SysRoleUserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * @param userId 角色id
     * @return 角色用户关系
     */
    List<SysRoleUserEntity> queryByUserId(Long userId);

    /**
     * 根据角色id列表，查询角色用户关系
     * @param userIds 角色id列表
     * @return 角色用户关系
     */
    Map<Long, Set<Long>> queryMapByUserIds(Collection<Long> userIds);

    /**
     * 建立用户角色关系
     * @param req 用户角色
     */
    void assignRoles(SysUserRoleReq req);

    /**
     * 授予/剥夺角色
     * @param req 用户角色
     */
    void updateRoles(SysUserRoleReq req);

    /**
     * 通过用户角色id列表删除角色
     * @param userIds 用户角色id列表
     */
    void deleteByUserIds(Collection<Long> userIds);

}
