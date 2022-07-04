package com.seeds.uc.service.impl;

import com.seeds.common.enums.Language;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.AuthCode;
import com.seeds.uc.dto.redis.TwoFactorAuth;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.service.*;
import com.seeds.uc.service.CacheService;
import com.seeds.uc.service.RiskControlService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.UserLanguageService;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.service.UserService;
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
    private RiskControlService riskControlService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLanguageService languageService;

    @Override
    public void sendUserCodeByUseType(UserDto userDto, AuthCodeUseTypeEnum useTypeEnum) {
        // generate a random code
        String otp = RandomUtil.getRandom6DigitsOTP();

        // 检查用户使用了选择了哪个安全项
        ClientAuthTypeEnum authTypeEnum = riskControlService.getUserAuthTypeEnum(userDto);

        // 设置此时登陆的安全项
        userDto.setAuthType(authTypeEnum);

        // TODO 调用渠道发送验证码, 发送失败的处理
        doSendUserCode(userDto, otp, useTypeEnum);

        String accountName =
                ClientAuthTypeEnum.getAccountNameByAuthType(
                        userDto.getPhone(),
                        userDto.getEmail(),
                        authTypeEnum);

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                accountName,
                userDto.getCountryCode(),
                userDto.getAuthType(),
                otp,
                useTypeEnum);
    }

    @Override
    // when trying to send to a new user with info submitted
    public void sendSmsWithUseType(String countryCode, String phone, AuthCodeUseTypeEnum useTypeEnum) {
        // generate a random code
        String otp = RandomUtil.getRandom6DigitsOTP();

        Language language = languageService.getLanguage();

        // TODO 发送短信 发送失败要不要处理
        doSendSmsCode(countryCode, phone, otp, language, useTypeEnum);

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                phone,
                countryCode,
                ClientAuthTypeEnum.PHONE,
                otp,
                useTypeEnum);
    }

    @Override
    public void sendSmsWithTokenAndUseType(String token, AuthCodeUseTypeEnum useTypeEnum) {
        // 取出之前login时放入二次验证redis的dto拿到详细信息
        TwoFactorAuth twoFactorAuth = cacheService.get2FAInfoWithToken(token);

        UserDto userDto = userService.getUserByUid(twoFactorAuth.getUserId());

        // generate a random code
        String otp = RandomUtil.getRandom6DigitsOTP();

        // TODO 发送短信，用user从db里拿的最新的信息
        sendSmsWithUseType(userDto.getCountryCode(), userDto.getPhone(), useTypeEnum);

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                twoFactorAuth.getAuthAccountName(),
                userDto.getCountryCode(),
                ClientAuthTypeEnum.PHONE,
                otp,
                useTypeEnum);
    }

    @Override
    public void sendEmailWithUseType(String address, AuthCodeUseTypeEnum useTypeEnum) {
        // generate a random code
        String otp = RandomUtil.getRandom6DigitsOTP();


        Language language = languageService.getLanguage();

        // TODO 发送邮件失败的处理
        doSendEmailCode(address, otp, language, useTypeEnum);

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                address,
                null,
                ClientAuthTypeEnum.EMAIL,
                otp,
                useTypeEnum);
    }

    @Override
    public void sendEmailWithTokenAndUseType(String token, AuthCodeUseTypeEnum useTypeEnum) {
        // 取出之前login时放入二次验证redis的dto拿到详细信息
        TwoFactorAuth twoFactorAuth = cacheService.get2FAInfoWithToken(token);

        UserDto userDto = userService.getUserByUid(twoFactorAuth.getUserId());

        // generate a random code
        String otp = RandomUtil.getRandom6DigitsOTP();

        // TODO发送
        sendEmailWithUseType(userDto.getEmail(), useTypeEnum);

        // store the auth code in auth code bucket
        cacheService.putAuthCode(
                userDto.getEmail(),
                null,
                ClientAuthTypeEnum.EMAIL,
                otp,
                useTypeEnum);
    }

    @Override
    public String verifySmsWithUseType(String countryCode, String phone, String otp, AuthCodeUseTypeEnum useTypeEnum) {
        AuthCode authCode =
                cacheService.getAuthCode(phone, useTypeEnum, ClientAuthTypeEnum.PHONE);
        if (authCode != null && StringUtils.isNotBlank(authCode.getCode()) && authCode.getCode().equals(otp)) {
            String token = RandomUtil.genRandomToken(phone);
            cacheService.putAuthToken(token, null, phone, ClientAuthTypeEnum.PHONE);
            return token;
        }
        throw new GenericException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
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

    // 调用send sms 和send email 方便调试
    // TODO 不用统一的user dto，改传多个必须的参数
    private boolean doSendUserCode(UserDto userDto, String otp, AuthCodeUseTypeEnum useType) {
        if (userDto == null
                || userDto.getAuthType() == null
                || !ClientAuthTypeEnum.NEED_SEND_CODE.contains(userDto.getAuthType())) {
            throw new IllegalArgumentException("invalid send code arguments");
        }

        Language language = languageService.getLanguage();


        if (ClientAuthTypeEnum.PHONE.equals(userDto.getAuthType()) && StringUtils.isNotBlank(userDto.getPhone())) {
            return doSendSmsCode(userDto.getCountryCode(), userDto.getPhone(), otp, language, useType);
        }

        if (ClientAuthTypeEnum.EMAIL.equals(userDto.getAuthType()) && StringUtils.isNotBlank(userDto.getEmail())) {
            return doSendEmailCode(userDto.getEmail(), otp, language, useType);
        }

        return false;
    }


    private boolean doSendSmsCode(String countryCode,
                                  String phone,
                                  String code,
                                  Language language,
                                  AuthCodeUseTypeEnum useType) {
//        GenericDto<Boolean> result = msgFeignClient.sendAuthCode(
//                AuthCodeMsgRequestDto.builder()
//                        .countryCode(countryCode)
//                        .phone(phone)
//                        .useType(useType.getCode())
//                        .language(language.getCode())
//                        .params(Collections.singletonMap("authCode", code))
//                        .build());
//        if(result != null){
//            return result.getData();
//        }
//        return false;
        return true;
    }

    private boolean doSendEmailCode(String email,
                                    String code,
                                    Language language,
                                    AuthCodeUseTypeEnum useType) {
//        GenericDto<Boolean> result = msgFeignClient.sendAuthCode(
//                AuthCodeMsgRequestDto.builder()
//                        .email(email)
//                        .useType(useType.getCode())
//                        .language(language.getCode())
//                        .params(Collections.singletonMap("authCode", code))
//                        .build());
//        if(result != null){
//            return result.getData();
//        }
//        return false;
        return true;
    }
}
