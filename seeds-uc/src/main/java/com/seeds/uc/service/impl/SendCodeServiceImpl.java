package com.seeds.uc.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.seeds.common.web.config.EmailProperties;
import com.seeds.uc.dto.AuthCode;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.service.ISendCodeService;
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
public class SendCodeServiceImpl implements ISendCodeService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private EmailProperties emailProperties;

    @Override
    public String sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum) {
        // generate a random code
        String otp = RandomUtil.getRandom6DigitsOTP();
        MailAccount account = new MailAccount();
        account.setHost(emailProperties.getHost());
        account.setPort(25);
        account.setAuth(true);
        account.setFrom(emailProperties.getFrom());
        account.setUser(emailProperties.getUser());
        account.setPass(emailProperties.getPass());
        MailUtil.send(account, CollUtil.newArrayList(address), "Bind the email verification code", "Bind the email verification code, note that it expires in 5 minutes:" + otp, false);
        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                address,
                null,
                ClientAuthTypeEnum.EMAIL,
                otp,
                useTypeEnum);
        return "Bind the email verification code, note that it expires in 5 minutes:" + otp;

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
        throw new GenericException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
    }
}
