package com.seeds.uc.web.send.service.impl;


import com.seeds.uc.model.send.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.model.user.enums.ClientAuthTypeEnum;
import com.seeds.uc.util.EMailUtil;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.web.cache.service.CacheService;
import com.seeds.uc.web.send.service.SendCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Slf4j
@Service
public class SendCodeServiceImpl implements SendCodeService {
    @Autowired
    private CacheService cacheService;


    @Override
    public void sendEmailWithTokenAndUseType(String token, AuthCodeUseTypeEnum useTypeEnum) {

    }

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
        return null;
    }
}
