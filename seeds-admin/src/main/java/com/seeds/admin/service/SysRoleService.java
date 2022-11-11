package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.request.SysRoleAddReq;
import com.seeds.admin.dto.request.SysRoleAssignReq;
import com.seeds.admin.dto.request.SysRoleModifyReq;
import com.seeds.admin.dto.request.SysRolePageReq;
import com.seeds.admin.dto.response.SysRoleResp;
import com.seeds.admin.entity.SysRoleEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统角色
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    /**
     * 分页查询系统角色
     * @param query 查询条件
     * @return 系统角色信息
     */
    IPage<SysRoleResp> queryPage(SysRolePageReq query);

    /**
     * 通过角色编码查询系统角色
     * @param roleCode 角色编码
     * @return 系统角色信息
     */
    SysRoleEntity queryByRoleCode(String roleCode);

    /**
     * 通过id列表查询系统角色
     * @param ids 角色id列表
     * @return 系统角色信息
     */
    Map<Long, String> queryMapByIds(Collection<Long> ids);

    /**
     * 添加系统角色
     * @param req 角色
     */
    void add(SysRoleAddReq req);

    /**
     * 修改系统角色
     * @param req 角色
     */
    void modify(SysRoleModifyReq req);

    /**
     * 批量删除系统角色
     * @param ids 角色id列表
     */
    void batchDelete(Collection<Long> ids);

    /**
     * 获取系统角色信息
     * @param id 角色id
     * @return 系统角色信息
     */
    SysRoleResp detail(Long id);

    /**
     * 获取系统角色信息
     * @return 系统角色信息
     */
    List<SysRoleResp> queryList();

    /**
     * 获取系统用户角色信息
     * @param userId 角色id
     * @return 系统用户角色信息
     */
    List<SysRoleResp> queryByUserId(Long userId);

    /**
     * 给用户分配指定的角色
     * @param req 角色id列表
     */
    void assign(SysRoleAssignReq req);

    /**
     * 根据角色编码查询用户id列表
     * @param roleCode 角色编码
     * @return 用户id列表
     */
    Set<Long> queryUsersByRole(String roleCode);

}
