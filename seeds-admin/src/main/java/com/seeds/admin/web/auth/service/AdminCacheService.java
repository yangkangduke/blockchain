package com.seeds.admin.web.auth.service;

import com.seeds.admin.dto.redis.LoginAdminUser;

/**
 * 管理后台redis缓存
 *
 * @author hang.yu
 * @date 2022/7/13
 */
public interface AdminCacheService {

    /**
     * 获取图片验证码缓存
     * @param uuid  唯一标识
     * @return 图片验证码
     */
    String getCaptchaCache(String uuid);

    /**
     * 获取图片验证码缓存
     * @param key  键
     * @param value 值
     */
    void putCaptchaCache(String key, String value);

    /**
     * 缓存后台用户登录信息
     * @param token 令牌
     * @param uid 用户id
     * @return 过期时间
     */
    Integer putAdminUserWithToken(String token, Long uid);

    /**
     * 获取后台用户登录信息
     * @param token 令牌
     */
    LoginAdminUser getAdminUserByToken(String token);

    /**
     * 用token来remove用户登陆态
     * @param token 令牌
     */
    void removeAdminUserByToken(String token);

    /**
     * 用用户编号来remove用户登陆态
     * @param userId 用户编号
     */
    void removeAdminUserByUserId(Long userId);

}
