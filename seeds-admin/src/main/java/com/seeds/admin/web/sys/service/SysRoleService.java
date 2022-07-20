package com.seeds.admin.web.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.sys.request.SysRoleAddReq;
import com.seeds.admin.dto.sys.request.SysRoleModifyReq;
import com.seeds.admin.dto.sys.request.SysRolePageReq;
import com.seeds.admin.dto.sys.response.SysRoleResp;
import com.seeds.admin.entity.sys.SysRoleEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统角色
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public interface SysRoleService {

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
     * 通过id查询系统角色
     * @param id 角色编号
     * @return 系统角色信息
     */
    SysRoleEntity queryById(Long id);

    /**
     * 通过id列表查询系统角色
     * @param ids 角色id列表
     * @return 系统角色信息
     */
    List<SysRoleEntity> queryByIds(Collection<Long> ids);

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

}
