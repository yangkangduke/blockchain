package com.seeds.uc.service;

import com.seeds.uc.dto.UserDto;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
public interface SendCodeService {
    void sendUserCodeByUseType(UserDto userDto, AuthCodeUseTypeEnum authCodeUseTypeEnum);

    // when trying to send to a new user with info submitted
    void sendSmsWithUseType(String countryCode, String phone, AuthCodeUseTypeEnum useTypeEnum);

    void sendSmsWithTokenAndUseType(String token, AuthCodeUseTypeEnum useTypeEnum);

    void sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum);

    void sendEmailWithTokenAndUseType(String token, AuthCodeUseTypeEnum useTypeEnum);

    String verifySmsWithUseType(String countryCode, String phone, String otp, AuthCodeUseTypeEnum useTypeEnum);

    String verifyEmailWithUseType(String address, String otp, AuthCodeUseTypeEnum useTypeEnum);
}
