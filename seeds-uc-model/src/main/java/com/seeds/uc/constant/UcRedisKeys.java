package com.seeds.uc.constant;

import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.experimental.UtilityClass;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@UtilityClass
public class UcRedisKeys {

    public final String UC_KEY_PREFIX = "uc:";

    /**
     * Key: uc:token:{ucToken} -> Value: {UserDto}
     * ucToken is the token returned when user login success
     */
    public final String UC_TOKEN_KEY_TEMPLATE = UC_KEY_PREFIX + "token:%s";

    // uid -> login token
    public final String UC_LOGIN_UID_KEY_TEMPLATE = UC_KEY_PREFIX + "login:uid:%s";

    /**
     * Key: uc:code:phone:login:{username} -> Value: {AuthCode}
     * username can be email or phone number
     */
    public final String UC_AUTH_PHONE_KEY_TEMPLATE = UC_KEY_PREFIX + "code:phone:%s:%s";

    public final String UC_AUTH_EMAIL_KEY_TEMPLATE = UC_KEY_PREFIX + "code:email:%s:%s";


    /**
     * Key: uc:2fa:{2faToken} -> Value: {TwoFactorAuth}
     * 2faToken is the token returned to user when user is trying to login and need 2fa
     */
    public final String UC_2FA_KEY_TEMPLATE = UC_KEY_PREFIX + "2fa:%s";


    public final String UC_SECURITY_AUTH_TOKEN_KEY_TEMPLATE = UC_KEY_PREFIX + "sec:token:%s";

    /**
     * if a verification passed, insert one of these
     */
    public final String UC_PHONE_AUTH_TOKEN_KEY_TEMPLATE = UC_KEY_PREFIX + "phone:token:%s";
    public final String UC_EMAIL_AUTH_TOKEN_KEY_TEMPLATE = UC_KEY_PREFIX + "email:token:%s";
    public final String UC_GOOGLE_AUTH_TOKEN_KEY_TEMPLATE = UC_KEY_PREFIX + "ga:token:%s";

    /**
     * if a ga secret is generated, insert one with user token
     */
    public final String UC_GENERATE_GOOGLE_AUTH_KEY_TEMPLATE = UC_KEY_PREFIX + "ga:user.token:%s";


    /**
     * return uc:token:{uid} as redis key in login
     */
    public String getUcTokenKey(String token) {
        return String.format(UC_TOKEN_KEY_TEMPLATE, token);
    }

    /**
     * 保存的内容为 uid -> login token
     */
    public String getUcLoginUidKey(Long uid) {
        return String.format(UC_LOGIN_UID_KEY_TEMPLATE, uid);
    }

    /**
     * Key: uc:code:phone:{username} -> Value: {AuthCode}
     * username can be email or phone number
     */
    public String getUcAuthCodeKeyWithAuthTypeAndUseType(String username,
                                                         AuthCodeUseTypeEnum useTypeEnum,
                                                         ClientAuthTypeEnum authTypeEnum) {
        if (ClientAuthTypeEnum.METAMASK.equals(authTypeEnum)) {
            return String.format(UC_AUTH_PHONE_KEY_TEMPLATE, useTypeEnum.getBrief(), username);
        } else if (ClientAuthTypeEnum.EMAIL.equals(authTypeEnum)) {
            return String.format(UC_AUTH_EMAIL_KEY_TEMPLATE, useTypeEnum.getBrief(), username);
        } else {
            throw new GenericException("Error generate uc code key for username "
                    + username
                    + " and use type "
                    + useTypeEnum.getBrief()
                    + " and auth type: "
                    + authTypeEnum.getDesc());
        }
    }

    /**
     * Key: uc:2fa:{2faToken} -> Value: {TwoFactorAuth}
     * 2faToken is the token returned to user when user is trying to login and need 2fa
     */
    public String getUcTwoFactorTokenKey(String twoFactorToken) {
        return String.format(UC_2FA_KEY_TEMPLATE, twoFactorToken);
    }

    /**
     * used for changing security settings
     */
    public String getUcSecurityAuthTokenKeyTemplate(String authToken) {
        return String.format(UC_SECURITY_AUTH_TOKEN_KEY_TEMPLATE, authToken);
    }

    /**
     * used for phone, email, ga bindings, set only when user ga verified
     */
    public String getUcAuthTokenKeyTemplate(String token, ClientAuthTypeEnum authTypeEnum) {
        if (ClientAuthTypeEnum.METAMASK.equals(authTypeEnum)) {
            return String.format(UC_PHONE_AUTH_TOKEN_KEY_TEMPLATE, token);
        } else if (ClientAuthTypeEnum.EMAIL.equals(authTypeEnum)) {
            return String.format(UC_EMAIL_AUTH_TOKEN_KEY_TEMPLATE, token);
        } else if (ClientAuthTypeEnum.GA.equals(authTypeEnum)) {
            return String.format(UC_GOOGLE_AUTH_TOKEN_KEY_TEMPLATE, token);
        } else {
            throw new GenericException("Error generate uc code key with"
                    + " token "
                    + token
                    + " and auth type: "
                    + authTypeEnum.getDesc());
        }
    }

    /**
     * used for generate uc ga
     */
    public String getUcGenerateGoogleAuthKeyTemplate(String token) {
        return String.format(UC_GENERATE_GOOGLE_AUTH_KEY_TEMPLATE, token);
    }
}