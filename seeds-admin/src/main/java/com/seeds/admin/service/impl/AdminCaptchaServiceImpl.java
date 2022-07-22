package com.seeds.admin.service.impl;

import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.service.AdminCacheService;
import com.seeds.admin.service.AdminCaptchaService;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码
 *
 * @author hang.yu
 * @date 2022/7/13
 */
@Service
public class AdminCaptchaServiceImpl implements AdminCaptchaService {

    @Autowired
    private AdminCacheService adminCacheService;

    @Value("${admin.login.opt.mock.flag:true}")
    private Boolean mockFlag;

    @Value("${admin.login.opt.mock.value:123456}")
    private String mockValue;

    @Override
    public void createCaptcha(HttpServletResponse response, String uuid) throws IOException {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", WhetherEnum.NO.value());

        //生成验证码
        SpecCaptcha captcha = new SpecCaptcha(150, 40);
        captcha.setLen(5);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        captcha.out(response.getOutputStream());

        //保存到缓存
        adminCacheService.putCaptchaCache(uuid, captcha.text());
    }

    @Override
    public boolean validateCaptcha(String uuid, String code) {
        //获取验证码
        String captcha = adminCacheService.getCaptchaCache(uuid);

        //效验成功
        if(code.equalsIgnoreCase(captcha)){
            return true;
        }

        return false;
    }

    @Override
    public boolean validateSmsCaptcha(String phone, String code) {
        if (mockFlag) {
            if (mockValue.equals(code)) {
                return true;
            }
        }
        return false;
    }

}