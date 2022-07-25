package com.seeds.uc.service;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
public interface ISendCodeService {

    String sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum);

    String verifyEmailWithUseType(String address, String otp, AuthCodeUseTypeEnum useTypeEnum);
}
