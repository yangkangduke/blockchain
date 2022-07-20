package com.seeds.admin.web.sys.service;


import java.util.Collection;
import java.util.List;

/**
 * 系统角色菜单
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public interface SysRoleMenuService {

    /**
     * 通过角色编号查询菜单
     * @param roleId 角色编号
     * @return 菜单列表
     */
    List<Long> queryMenuByRoleId(Long roleId);

    /**
     * 通过角色编号列表查询菜单
     * @param roleIds 角色编号列表
     * @return 菜单列表
     */
    List<Long> queryMenuByRoleIds(Collection<Long> roleIds);

    /**
     * 通过菜单编号删除关联
     * @param menuId 菜单编号
     */
    void deleteByMenuId(Long menuId);

    /**
     * 保存或修改
     * @param roleId      角色ID
     * @param menuIds  菜单ID列表
     */
    void saveOrUpdate(Long roleId, Collection<Long> menuIds);

    /**
     * 根据角色id，删除角色菜单关系
     * @param roleIds 角色ids
     */
    void deleteByRoleIds(Collection<Long> roleIds);

}
