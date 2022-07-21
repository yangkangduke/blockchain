package com.seeds.admin.web.sys.service;

import com.seeds.admin.dto.common.ListReq;
import com.seeds.admin.dto.sys.request.SysMenuAddReq;
import com.seeds.admin.dto.sys.request.SysMenuModifyReq;
import com.seeds.admin.dto.sys.response.SysMenuBriefResp;
import com.seeds.admin.dto.sys.response.SysMenuResp;
import com.seeds.admin.entity.sys.SysMenuEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * 系统角色
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public interface SysMenuService {

    /**
     * 添加系统菜单
     * @param req 菜单
     */
    void add(SysMenuAddReq req);

    /**
     * 编辑系统菜单
     * @param req 菜单
     */
    void modify(SysMenuModifyReq req);

    /**
     * 批量删除系统菜单
     * @param req 菜单编号列表
     */
    void batchDelete(ListReq req);

    /**
     * 统计子菜单
     * @param codes 菜单code列表
     * @return 子菜单数目
     */
    Long countKidsByCodes(Collection<String> codes);

    /**
     * 查询菜单列表
     * @param type 菜单类型
     * @return 菜单列表
     */
    List<SysMenuResp> queryRespList(Integer type);

    /**
     * 查询菜单列表
     * @param type 菜单类型
     * @return 菜单列表
     */
    List<SysMenuEntity> queryList(Integer type);

    /**
     * 菜单信息
     * @param id 编号
     * @return 菜单列表
     */
    SysMenuResp detail(Long id);

    /**
     * 菜单信息
     * @param ids 菜单id列表
     * @return 菜单code列表
     */
    Set<String> queryCodesByIds(Collection<Long> ids);

    /**
     * 菜单信息
     * @param code 编码
     * @return 菜单
     */
    SysMenuEntity queryByMenuCode(String code);

    /**
     * 根据用户id查询菜单列表
     * @param type 菜单类型
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenuResp> queryByUserId(Integer type, Long userId);

    /**
     * 查询权限列表
     * @return 权限列表
     */
    List<String> queryPermissionsList();

    /**
     * 根据用户id查询权限列表
     * @param userId 用户id
     * @return 权限列表
     */
    List<String> queryUserPermissionsList(Long userId);

    /**
     * 根据用户id查询菜单列表
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenuEntity> queryMenuListByUserId(Long userId);

    /**
     * 获取用户有权限的菜单列表
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenuBriefResp> getUserMenuList(Long userId);

}
