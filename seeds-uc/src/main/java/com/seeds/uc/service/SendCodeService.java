package com.seeds.uc.service;

import com.seeds.uc.dto.UserDto;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;

/**
* @author yk
 * @date 2020/8/1
 */
public interface SendCodeService {
    void sendUserCodeByUseType(UserDto userDto, AuthCodeUseTypeEnum authCodeUseTypeEnum);

    void sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum);

    void sendEmailWithTokenAndUseType(String token, AuthCodeUseTypeEnum useTypeEnum);

    String verifyEmailWithUseType(String address, String otp, AuthCodeUseTypeEnum useTypeEnum);
}
