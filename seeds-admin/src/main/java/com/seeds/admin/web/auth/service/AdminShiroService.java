package com.seeds.admin.web.auth.service;


import java.util.Set;

/**
 * 管理后台权限认证
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public interface AdminShiroService {

    /**
     * 获取用户权限列表
     * @param userId 用户编号
     * @return 用户权限列表
     */
    Set<String>  getUserPermissions(Long userId);

}
