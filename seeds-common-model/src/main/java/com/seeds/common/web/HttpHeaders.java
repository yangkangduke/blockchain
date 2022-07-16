package com.seeds.common.web;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpHeaders {

    /**
     * User id which will be added after gateway to micro service
     */
    public static final String INTERNAL_USER_ID = "internal-user-id";

    /**
     * user token
     */
    public static final String USER_TOKEN = "seeds-user-token";

    /**
     * 管理后台用户id
     */
    public static final String ADMIN_USER_ID = "admin-user-id";

    /**
     * 管理后台用户token
     */
    public static final String ADMIN_USER_TOKEN = "seeds-admin-user-token";

}
