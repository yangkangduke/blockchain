package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.response.AdminUserResp;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysUserAddReq;
import com.seeds.admin.dto.request.SysUserModifyReq;
import com.seeds.admin.dto.request.SysUserPageReq;
import com.seeds.admin.dto.response.SysUserBriefResp;
import com.seeds.admin.dto.response.SysUserResp;
import com.seeds.admin.entity.SysUserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author hang.yu
 * @date 2022/7/13
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 通过手机号查询系统用户
     * @param mobile 手机号
     * @return 系统用户信息
     */
    SysUserEntity queryByMobile(String mobile);

    /**
     * 通过账号查询系统用户
     * @param account 账号
     * @return 系统用户信息
     */
    SysUserEntity queryByAccount(String account);

    /**
     * 通过id查询系统用户
     * @param userId 用户id
     * @return 系统用户信息
     */
    SysUserEntity queryById(Long userId);

    /**
     * 分页查询系统用户
     * @param query 查询条件
     * @return 系统用户信息
     */
    IPage<SysUserResp> queryPage(SysUserPageReq query);

    /**
     * 添加系统用户
     * @param user 用户信息
     * @return 系统用户信息
     */
    SysUserEntity add(SysUserAddReq user);

    /**
     * 修改系统用户
     * @param user 用户信息
     * @return 系统用户信息
     */
    SysUserEntity modify(SysUserModifyReq user);

    /**
     * 通过id批量查询系统用户
     * @param ids 用户id
     * @return 系统用户信息
     */
    List<SysUserEntity> queryByIds(Collection<Long> ids);

    /**
     * 通过id批量查询系统用户名称
     * @param ids 用户id
     * @return 系统用户名称信息
     */
    Map<Long, String> queryNameMapByIds(Collection<Long> ids);

    /**
     * 批量删除用户
     * @param ids 用户编号集合
     */
    void batchDelete(Collection<Long> ids);

    /**
     * 修改密码
     * @param adminUser 系统用户
     * @param newPassword 新密码
     */
    void updatePassword(SysUserEntity adminUser, String newPassword);

    /**
     * 批量启用/停用用户
     * @param req 用户编号和状态集合
     */
    void enableOrDisable(List<SwitchReq> req);

    /**
     * 获取用户角色、菜单等信息
     * @param userId 用户编号
     * @return 用户信息
     */
    AdminUserResp queryLoginUserInfo(Long userId);

    /**
     * 根据id获取用户信息
     * @param id 用户编号
     * @return 用户信息
     */
    SysUserResp detail(Long id);

    /**
     * 根据手机号获取用户简略信息
     * @param mobile 用户手机号
     * @return 用户简略信息
     */
    SysUserBriefResp brief(String mobile);

    /**
     * metamask获取随机数
     * @param publicAddress
     * @param adminUser
     * @return
     */
    String metamaskNonce(String publicAddress, Long userId);

    /**
     * 更新metamask信息
     */
    Boolean updateMetaMask(Long userId);

    /**
     * 删除metaemask相关信息
     * @param userId
     * @return
     */
    Boolean deleteMetaMask(Long userId);
}
