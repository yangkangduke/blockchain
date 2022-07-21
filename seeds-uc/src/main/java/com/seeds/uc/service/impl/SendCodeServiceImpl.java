package com.seeds.uc.service.impl;


import com.seeds.uc.dto.AuthCode;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.util.EMailUtil;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Slf4j
@Service
public class SendCodeServiceImpl implements SendCodeService {
    @Autowired
    private CacheService cacheService;

    @Override
    public void sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum) {
        // generate a random code
        String otp = RandomUtil.getRandom6DigitsOTP();
        EMailUtil.send(address, otp);

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                address,
                null,
                ClientAuthTypeEnum.EMAIL,
                otp,
                useTypeEnum);
    }

    @Override
    public String verifyEmailWithUseType(String address, String otp, AuthCodeUseTypeEnum useTypeEnum) {
        AuthCode authCode =
                cacheService.getAuthCode(address, useTypeEnum, ClientAuthTypeEnum.EMAIL);
        if (authCode != null && StringUtils.isNotBlank(authCode.getCode()) && authCode.getCode().equals(otp)) {
            String token = RandomUtil.genRandomToken(address);
            cacheService.putAuthToken(token, null, address, ClientAuthTypeEnum.EMAIL);
            return token;
        }
        throw new GenericException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
    }
}
