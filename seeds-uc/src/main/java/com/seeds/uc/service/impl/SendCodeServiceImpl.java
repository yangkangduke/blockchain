package com.seeds.uc.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.seeds.common.web.config.EmailProperties;
import com.seeds.uc.config.ResetPasswordProperties;
import com.seeds.uc.constant.UcConstant;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.AuthCodeDTO;
import com.seeds.uc.dto.redis.TwoFactorAuth;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.exceptions.SendAuthCodeException;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.util.DigestUtil;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    private UcUserMapper ucUserMapper;
    @Autowired
    private ResetPasswordProperties resetPasswordProperties;

    @Override
    public void sendUserCodeByUseType(UserDto userDto, AuthCodeUseTypeEnum useTypeEnum) {
        // generate a random code
//        String otp = RandomUtil.getRandom6DigitsOTP();
        // todo 后面换一下
        String otp = "123456";

        // 设置此时登陆的安全项
        userDto.setAuthType(userDto.getAuthType());

        doSendUserCode(userDto, otp, useTypeEnum);

        String accountName =  ClientAuthTypeEnum.getAccountNameByAuthType(
                        userDto.getPhone(),
                        userDto.getEmail(),
                        userDto.getAuthType());

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                accountName,
                userDto.getAuthType(),
                otp,
                useTypeEnum);
    }

    @Override
    public void sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum) {
        // generate a random code
//        String otp = RandomUtil.getRandom6DigitsOTP();
        // todo 后面换一下
        String otp = "123456";
        doSendEmailCode(address, otp, useTypeEnum);

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                address,
                ClientAuthTypeEnum.EMAIL,
                otp,
                useTypeEnum);
    }

    @Override
    public void sendEmailWithTokenAndUseType(String token, AuthCodeUseTypeEnum useTypeEnum) {
        // 取出之前login时放入二次验证redis的dto拿到详细信息
        TwoFactorAuth twoFactorAuth = cacheService.get2FAInfoWithToken(token);

        UcUser userDto = ucUserMapper.selectById(twoFactorAuth.getUserId());

        // generate a random code
//        String otp = RandomUtil.getRandom6DigitsOTP();
        // todo 后面换一下
        String otp = "123456";
        // TODO发送
        sendEmailWithUseType(userDto.getEmail(), useTypeEnum);

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                userDto.getEmail(),
                ClientAuthTypeEnum.EMAIL,
                otp,
                useTypeEnum);
    }


    @Override
    public String verifyEmailWithUseType(String address, String otp, AuthCodeUseTypeEnum useTypeEnum) {
        AuthCodeDTO authCode = cacheService.getAuthCode(address, useTypeEnum, ClientAuthTypeEnum.EMAIL);
        if (authCode != null && StringUtils.isNotBlank(authCode.getCode()) && authCode.getCode().equals(otp)) {
            String token = RandomUtil.genRandomToken(address);
            cacheService.putAuthToken(token, null, address, ClientAuthTypeEnum.EMAIL);
            return token;
        }
        throw new GenericException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
    }

    // 调用send sms 和send email 方便调试
    private boolean doSendUserCode(UserDto userDto, String otp, AuthCodeUseTypeEnum useType) {
        if (userDto == null
                || userDto.getAuthType() == null
                || !ClientAuthTypeEnum.NEED_SEND_CODE.contains(userDto.getAuthType())) {
            throw new IllegalArgumentException("invalid send code arguments");
        }

        if (ClientAuthTypeEnum.EMAIL.equals(userDto.getAuthType()) && StringUtils.isNotBlank(userDto.getEmail())) {
            return doSendEmailCode(userDto.getEmail(), otp, useType);
        }

        return false;
    }

    private boolean doSendEmailCode(String email, String code, AuthCodeUseTypeEnum useType) {
        if (useType.equals(AuthCodeUseTypeEnum.REGISTER)) {
            MailUtil.send(this.createMailAccount(), CollUtil.newArrayList(email), UcConstant.REGISTER_EMAIL_SUBJECT, StrFormatter.format(UcConstant.REGISTER_EMAIL_CONTENT, 5, code), false);
        } else if (useType.equals(AuthCodeUseTypeEnum.RESET_PASSWORD)) {
            String encode = Base64.encode(DigestUtil.Encrypt(StrFormatter.format(UcConstant.FORGOT_PASSWORD_EMAIL_LINK_SYMBOL, email ,code)));
            MailUtil.send(this.createMailAccount(), CollUtil.newArrayList(email), UcConstant.FORGOT_PASSWORD_EMAIL_SUBJECT, StrFormatter.format(UcConstant.FORGOT_PASSWORD_EMAIL_CONTENT,5, StrFormatter.format(resetPasswordProperties.getUrl(),encode, email)), true);
        } else if (useType.equals(AuthCodeUseTypeEnum.LOGIN)) {
            MailUtil.send(this.createMailAccount(), CollUtil.newArrayList(email), UcConstant.LOGIN_EMAIL_VERIFY_SUBJECT, StrFormatter.format(UcConstant.LOGIN_EMAIL_VERIFY_CONTENT, 5, code), false);
        } else {
            throw new SendAuthCodeException(UcErrorCodeEnum.ERR_502_ILLEGAL_ARGUMENTS);
        }

        return true;
    }

    private MailAccount createMailAccount() {
        MailAccount account = new MailAccount();
        account.setHost(emailProperties.getHost());
        account.setAuth(true);
        account.setSslEnable(true);
        account.setFrom(emailProperties.getFrom());
        account.setUser(emailProperties.getUser());
        account.setPass(emailProperties.getPass());
        return account;
    }
}
