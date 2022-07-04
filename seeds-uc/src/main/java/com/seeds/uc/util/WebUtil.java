package com.seeds.uc.util;

import com.seeds.common.web.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
public class WebUtil {
    private WebUtil() {
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_TOKEN);
    }
}
