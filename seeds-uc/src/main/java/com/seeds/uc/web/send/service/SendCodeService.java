package com.seeds.uc.web.send.service;

import com.seeds.uc.model.send.enums.AuthCodeUseTypeEnum;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
public interface SendCodeService {

    void sendEmailWithTokenAndUseType(String token, AuthCodeUseTypeEnum useTypeEnum);

    void sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum);

    String verifyEmailWithUseType(String address, String otp, AuthCodeUseTypeEnum useTypeEnum);
}
