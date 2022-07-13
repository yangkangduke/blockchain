package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.SysAdminUserDto;
import com.seeds.admin.dto.request.AdminUserQuery;
import com.seeds.admin.entity.SysAdminUserEntity;

/**
 * 系统用户
 *
 * @author hang.yu
 * @date 2022/7/13
 */
public interface SysAdminUserService {

    /**
     * 通过手机号查询系统用户
     * @param mobile 手机号
     * @return 系统用户信息
     */
    SysAdminUserEntity queryByMobile(String mobile);

    /**
     * 通过账号查询系统用户
     * @param account 账号
     * @return 系统用户信息
     */
    SysAdminUserEntity queryByAccount(String account);

    /**
     * 通过id查询系统用户
     * @param userId 用户id
     * @return 系统用户信息
     */
    SysAdminUserDto queryDtoById(Long userId);

    /**
     * 分页查询系统用户
     * @param query 查询条件
     * @return 系统用户信息
     */
    IPage<SysAdminUserDto> queryPage(AdminUserQuery query);

    /**
     * 添加系统用户
     * @param user 用户信息
     * @return 系统用户信息
     */
    SysAdminUserDto add(SysAdminUserDto user);

}
