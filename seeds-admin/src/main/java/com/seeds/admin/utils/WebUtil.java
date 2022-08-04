package com.seeds.admin.utils;

import com.seeds.common.web.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yk
 * @date 2022/8/4
 */
public class WebUtil {
    private WebUtil() {
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.ADMIN_USER_TOKEN);
    }
}
