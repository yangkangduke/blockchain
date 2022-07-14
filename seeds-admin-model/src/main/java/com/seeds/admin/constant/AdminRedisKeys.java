package com.seeds.admin.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AdminRedisKeys {

    public final String ADMIN_KEY_PREFIX = "admin:";

    public final String ADMIN_TOKEN_KEY_TEMPLATE = ADMIN_KEY_PREFIX + "token:%s";

    public final String ADMIN_LOGIN_UID_KEY_TEMPLATE = ADMIN_KEY_PREFIX + "login:uid:%s";

    public final String ADMIN_LOGIN_CAPTCHA_KEY_PREFIX = ADMIN_KEY_PREFIX + "login:captcha:%S";

    /**
     * 拼接管理后台用户token的key
     */
    public String getAdminUserTokenKey(String token) {
        return String.format(ADMIN_TOKEN_KEY_TEMPLATE, token);
    }

    /**
     * 拼接管理后台用户id的key
     */
    public String getAdminUserIdKey(Long uid) {
        return String.format(ADMIN_LOGIN_UID_KEY_TEMPLATE, uid);
    }

    /**
     * 拼接图形验证码缓存的key
     */
    public String getAdminLoginCaptchaKeyTemplate(String key) {
        return String.format(ADMIN_LOGIN_CAPTCHA_KEY_PREFIX, key);
    }

}