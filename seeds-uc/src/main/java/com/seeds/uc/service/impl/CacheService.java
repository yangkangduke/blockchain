package com.seeds.uc.service.impl;


import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.dto.redis.AuthCodeDTO;
import com.seeds.uc.dto.redis.AuthTokenDTO;
import com.seeds.uc.dto.redis.ForgotPasswordCodeDTO;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.redis.TwoFactorAuth;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/29
 */

@Slf4j
@Service
public class CacheService {
    @Value("${uc.sso.redis.expire:259200}")
    private Integer loginExpireAfter;

    // auth code
    @Value("${uc.code.auth.expire:300}")
    private Integer authCodeExpireAfter;

    // 2FA token的过期时间
    @Value("${uc.code.2fa.expire:300}")
    private Integer twoFaCodeExpireAfter;

    // 安全项相关的token过期时间
    @Value("${uc.security.auth.expire:300}")
    private Integer securityAuthTokenExpireAfter;

    // auth_token过期时间
    @Value("${uc.auth.token.expire:300}")
    private Integer authTokenExpireAfter;

    // ga生成，绑定 的过期时间
    @Value("${uc.ga.expire:300}")
    private Integer googleAuthExpireAfter;

    @Autowired
    private RedissonClient redisson;

    /**
     * key为2FA的token，用于账户安全的账户2FA校验
     */
    public void put2FAInfoWithTokenAndUserAndAuthType(String token,
                                                      Long userId,
                                                      String name,
                                                      ClientAuthTypeEnum authTypeEnum) {
        Long expireAt = System.currentTimeMillis() + twoFaCodeExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcTwoFactorTokenKey(token);
        RBucket<TwoFactorAuth> authDtoRBucket = redisson.getBucket(key);
        TwoFactorAuth authDto =
                TwoFactorAuth.builder()
                        .userId(userId)
                        .authAccountName(name)
                        .authType(authTypeEnum)
                        .expireAt(expireAt)
                        .build();
        authDtoRBucket.set(authDto, twoFaCodeExpireAfter, TimeUnit.SECONDS);
        log.info("CacheService - putAuthCodeByNameAndAuthTypeAndUseType - put key: {}, value: {}", key, authDto);
    }

    public TwoFactorAuth get2FAInfoWithToken(String token) {
        String key = UcRedisKeysConstant.getUcTwoFactorTokenKey(token);
        RBucket<TwoFactorAuth> twoFactorAuthDto = redisson.getBucket(key);
        return twoFactorAuthDto.get();
    }

    /**
     * key uc:token:{userId}
     * uc token 也会写在用户的cookie里
     * 如果用户没有登陆活着已过期，会返回null
     */
    public void putUserWithTokenAndLoginName(String token, Long uid, String loginName) {
        Long expireAt = System.currentTimeMillis() + loginExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcTokenKey(token);
        String loginUidKey = UcRedisKeysConstant.getUcLoginUidKey(uid);
        LoginUserDTO loginUser = LoginUserDTO.builder()
                .userId(uid)
                .loginName(loginName)
                .expireAt(expireAt)
                .build();

        redisson.getBucket(key).set(loginUser, loginExpireAfter, TimeUnit.SECONDS);

        // set uid -> token
        redisson.getBucket(loginUidKey).set(token, loginExpireAfter, TimeUnit.SECONDS);

        log.info("CacheService - putUserWithToken - put key: {}, value: {}", key, loginUser);
    }

    /**
     * key为用户名(手机或邮箱)的cache，用于绑定账户等用到的验证码校验
     */
    public void putAuthCode(String name,
                            String countryCode,
                            ClientAuthTypeEnum authType,
                            String code,
                            AuthCodeUseTypeEnum useType) {
        Long expireAt = System.currentTimeMillis() + authCodeExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcAuthCodeKeyWithAuthTypeAndUseType(name, useType, authType);
        AuthCodeDTO authCode = AuthCodeDTO.builder()
                .authType(authType)
                .code(code)
                .expireAt(expireAt)
                .key(key)
                .name(name)
                .countryCode(countryCode)
                .useType(useType)
                .build();
        redisson.getBucket(authCode.getKey()).set(
                authCode,
                authCodeExpireAfter,
                TimeUnit.SECONDS);
        log.info("CacheService - putAuthCodeByNameAndAuthTypeAndUseType - put key: {}, value: {}", key, authCode);
    }

    public AuthCodeDTO getAuthCode(String name,
                                   AuthCodeUseTypeEnum useType,
                                   ClientAuthTypeEnum authTypeEnum) {
        String key = UcRedisKeysConstant.getUcAuthCodeKeyWithAuthTypeAndUseType(name, useType, authTypeEnum);
        RBucket<AuthCodeDTO> authCodeDtoRBucket = redisson.getBucket(key);
        return authCodeDtoRBucket.get();
    }

    /**
     * key为用户名(邮箱)的cache，用于忘记密码功能校验
     */
    public void putForgotPasswordCode(String account, String otp, long expireAt) {
        String key = UcRedisKeysConstant.getUcKeyForgotPassword(account);
        ForgotPasswordCodeDTO forgotPasswordCode = ForgotPasswordCodeDTO.builder()
                .key(key)
                .code(otp)
                .account(account)
                .expireAt(expireAt)
                .build();
        redisson.getBucket(forgotPasswordCode.getKey()).set(
                forgotPasswordCode,
                expireAt,
                TimeUnit.MILLISECONDS);
        log.info("CacheService - putForgotPasswordCode - put key: {}, value: {}", key, otp);
    }

    public ForgotPasswordCodeDTO getForgotPasswordCode(String account) {
        String key = UcRedisKeysConstant.getUcKeyForgotPassword(account);
        RBucket<ForgotPasswordCodeDTO> redissonBucket = redisson.getBucket(key);
        return redissonBucket.get();
    }

    /**
     * will insert once a auth type is verified and return a token
     */
    public void putAuthToken(String token,
                             String secret,
                             String accountName,
                             ClientAuthTypeEnum authTypeEnum) {
        Long expireAt = System.currentTimeMillis() + authTokenExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcAuthTokenKeyTemplate(token, authTypeEnum);
        RBucket<AuthTokenDTO> authTokenRBucket = redisson.getBucket(key);
        AuthTokenDTO authToken =
                AuthTokenDTO.builder()
                        .accountName(accountName)
                        .secret(secret)
                        .expireAt(expireAt)
                        .build();
        authTokenRBucket.set(authToken, authTokenExpireAfter, TimeUnit.SECONDS);
        log.info("CacheService - putAuthToken - put key: {}, value: {}", key, authToken);
    }

    /**
     * key uc:token:{userId}
     * uc token 也会写在用户的cookie里
     * 如果用户没有登陆活着已过期，会返回null
     */
    public LoginUserDTO getUserByToken(String token) {
        String key = UcRedisKeysConstant.getUcTokenKey(token);
        RBucket<LoginUserDTO> value = redisson.getBucket(key);
        return value.get();
    }

}