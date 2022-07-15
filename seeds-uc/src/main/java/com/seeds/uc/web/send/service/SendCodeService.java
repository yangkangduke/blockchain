package com.seeds.uc.web.send.service;

import com.seeds.uc.model.send.enums.AuthCodeUseTypeEnum;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
public interface SendCodeService {

    void sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum);

    String verifyEmailWithUseType(String address, String otp, AuthCodeUseTypeEnum useTypeEnum);
}
