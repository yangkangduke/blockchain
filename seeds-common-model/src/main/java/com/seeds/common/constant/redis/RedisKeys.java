package com.seeds.common.constant.redis;

import lombok.experimental.UtilityClass;

/**
 * @author hang.yu
 * @date 2022/07/18
 */
@UtilityClass
public class RedisKeys {

    public final String UC_KEY_PREFIX = "uc:";

    public final String ADMIN_KEY_PREFIX = "admin:";

    // uid -> login token
    public final String UC_LOGIN_UID_KEY_TEMPLATE = UC_KEY_PREFIX + "login:uid:%s";

    public final String UC_TOKEN_KEY_TEMPLATE = UC_KEY_PREFIX + "token:%s";

    public final String ADMIN_LOGIN_UID_KEY_TEMPLATE = ADMIN_KEY_PREFIX + "login:uid:%s";

    /**
     * return uc:token:{uid} as redis key in login
     */
    public String getUcTokenKey(String token) {
        return String.format(UC_TOKEN_KEY_TEMPLATE, token);
    }

    /**
     * 保存的内容为 uid -> login token
     */
    public String getUcLoginUidKey(Long uid) {
        return String.format(UC_LOGIN_UID_KEY_TEMPLATE, uid);
    }

    /**
     * 拼接管理后台用户id的key
     */
    public String getAdminUserIdKey(Long uid) {
        return String.format(ADMIN_LOGIN_UID_KEY_TEMPLATE, uid);
    }

}