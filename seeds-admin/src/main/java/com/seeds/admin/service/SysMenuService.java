package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysMenuAddReq;
import com.seeds.admin.dto.request.SysMenuModifyReq;
import com.seeds.admin.dto.response.SysMenuBriefResp;
import com.seeds.admin.dto.response.SysMenuResp;
import com.seeds.admin.entity.SysMenuEntity;
import com.seeds.admin.entity.SysUserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * 系统角色
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public interface SysMenuService extends IService<SysMenuEntity> {

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
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenuResp> queryByUserId(Long userId);

    /**
     * 查询显示的菜单列表
     * @return 菜单列表
     */
    List<SysMenuResp> queryShowMenu();

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
     * @param user 用户
     * @return 菜单列表
     */
    List<SysMenuBriefResp> getUserMenuList(SysUserEntity user);

    /**
     * 批量启用/停用菜单
     * @param req 菜单编号和状态集合
     */
    void enableOrDisable(List<SwitchReq> req);

}
