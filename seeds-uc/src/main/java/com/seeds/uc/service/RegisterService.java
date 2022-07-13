package com.seeds.uc.service;

import com.seeds.uc.dto.request.EmailCodeSendReq;
import com.seeds.uc.dto.request.EmailCodeVerifyReq;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
public interface RegisterService {

    /**
     * 发送邮箱验证码
     * @param sendReq
     */
    void sendEmailCode(EmailCodeSendReq sendReq);

    /**
     * 邮箱验证码校验
     * @param verifyReq
     */
    void verifyRegisterCode(EmailCodeVerifyReq verifyReq);
}