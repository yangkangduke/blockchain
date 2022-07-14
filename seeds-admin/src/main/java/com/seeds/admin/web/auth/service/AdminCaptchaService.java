package com.seeds.admin.web.auth.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码
 *
 * @author hang.yu
 * @date 2022/7/13
 */
public interface AdminCaptchaService {

    /**
     * 图片验证码
     * @param uuid  唯一标识
     * @param response response
     */
    void createCaptcha(HttpServletResponse response, String uuid) throws IOException;

    /**
     * 验证码效验
     * @param uuid 唯一标识
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validateCaptcha(String uuid, String code);

    /**
     * 手机验证码效验
     * @param phone 手机号
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validateSmsCaptcha(String phone, String code);

}
