package com.seeds.uc.service;

import com.seeds.uc.constant.UcRedisKeys;
import com.seeds.uc.dto.redis.*;
import com.seeds.uc.dto.redis.AuthCode;
import com.seeds.uc.dto.redis.AuthToken;
import com.seeds.uc.dto.redis.GenGoogleAuth;
import com.seeds.uc.dto.redis.LoginUser;
import com.seeds.uc.dto.redis.SecurityAuth;
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
     * key uc:token:{userId}
     * uc token 也会写在用户的cookie里
     * 如果用户没有登陆活着已过期，会返回null
     */
    public void putUserWithTokenAndLoginName(String token, Long uid, String loginName) {
        Long expireAt = System.currentTimeMillis() + loginExpireAfter * 1000;
        String key = UcRedisKeys.getUcTokenKey(token);
        String loginUidKey = UcRedisKeys.getUcLoginUidKey(uid);
        LoginUser loginUser = LoginUser.builder()
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
     * key uc:token:{userId}
     * uc token 也会写在用户的cookie里
     * 如果用户没有登陆活着已过期，会返回null
     */
    public LoginUser getUserByToken(String token) {
        String key = UcRedisKeys.getUcTokenKey(token);
        RBucket<LoginUser> value = redisson.getBucket(key);
        return value.get();
    }

    // 用uid来remove用户登陆态
    public void removeUserByUid(Long uid) {
        String key = UcRedisKeys.getUcLoginUidKey(uid);
        RBucket<String> value = redisson.getBucket(key);
        String token = value.get();
        removeUserByToken(token);
        value.delete();
    }

    // 用token来remove用户登陆态
    public void removeUserByToken(String token) {
        String key = UcRedisKeys.getUcTokenKey(token);
        RBucket<LoginUser> value = redisson.getBucket(key);
        removeUserLoginUidTokenMapping(value.get().getUserId());
        value.delete();
    }

    private void removeUserLoginUidTokenMapping(Long uid) {
        String key = UcRedisKeys.getUcLoginUidKey(uid);
        RBucket<String> value = redisson.getBucket(key);
        value.delete();
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
        String key = UcRedisKeys.getUcAuthCodeKeyWithAuthTypeAndUseType(name, useType, authType);
        AuthCode authCode = AuthCode.builder()
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

    public AuthCode getAuthCode(String name,
                                AuthCodeUseTypeEnum useType,
                                ClientAuthTypeEnum authTypeEnum) {
        String key = UcRedisKeys.getUcAuthCodeKeyWithAuthTypeAndUseType(name, useType, authTypeEnum);
        RBucket<AuthCode> authCodeDtoRBucket = redisson.getBucket(key);
        return authCodeDtoRBucket.get();
    }

    /**
     * key为2FA的token，用于账户安全的账户2FA校验
     */
    public void put2FAInfoWithTokenAndUserAndAuthType(String token,
                                                      Long userId,
                                                      String name,
                                                      ClientAuthTypeEnum authTypeEnum) {
        Long expireAt = System.currentTimeMillis() + twoFaCodeExpireAfter * 1000;
        String key = UcRedisKeys.getUcTwoFactorTokenKey(token);
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
        String key = UcRedisKeys.getUcTwoFactorTokenKey(token);
        RBucket<TwoFactorAuth> twoFactorAuthDto = redisson.getBucket(key);
        return twoFactorAuthDto.get();
    }

    /**
     * key 为 auth_code 的token，用于账户安全项如phone， email， ga的改动的策略验证
     */
    public void putSecurityAuthTokenWithUidAndUseType(String token,
                                                      Long uid,
                                                      AuthCodeUseTypeEnum useTypeEnum) {
        Long expireAt = System.currentTimeMillis() + securityAuthTokenExpireAfter * 1000;
        String key = UcRedisKeys.getUcSecurityAuthTokenKeyTemplate(token);
        RBucket<SecurityAuth> securityAuthRBucket = redisson.getBucket(key);
        SecurityAuth authToken =
                SecurityAuth.builder()
                        .uid(uid)
                        .useType(useTypeEnum)
                        .expireAt(expireAt)
                        .build();
        securityAuthRBucket.set(authToken, securityAuthTokenExpireAfter, TimeUnit.SECONDS);
        log.info("CacheService - putSecurityAuthTokenWithUidAndUseType - put key: {}, value: {}", key, authToken);
    }

    public SecurityAuth getSecurityAuthWithToken(String token) {
        String key = UcRedisKeys.getUcSecurityAuthTokenKeyTemplate(token);
        RBucket<SecurityAuth> securityAuthRBucket = redisson.getBucket(key);
        return securityAuthRBucket.get();
    }

    /**
     * will insert once a auth type is verified and return a token
     */
    public void putAuthToken(String token,
                             String secret,
                             String accountName,
                             ClientAuthTypeEnum authTypeEnum) {
        Long expireAt = System.currentTimeMillis() + authTokenExpireAfter * 1000;
        String key = UcRedisKeys.getUcAuthTokenKeyTemplate(token, authTypeEnum);
        RBucket<AuthToken> authTokenRBucket = redisson.getBucket(key);
        AuthToken authToken =
                AuthToken.builder()
                        .accountName(accountName)
                        .secret(secret)
                        .expireAt(expireAt)
                        .build();
        authTokenRBucket.set(authToken, authTokenExpireAfter, TimeUnit.SECONDS);
        log.info("CacheService - putAuthToken - put key: {}, value: {}", key, authToken);
    }

    public AuthToken getAuthTokenDetailWithToken(String token, ClientAuthTypeEnum authTypeEnum) {
        String key = UcRedisKeys.getUcAuthTokenKeyTemplate(token, authTypeEnum);
        RBucket<AuthToken> googleAuthRBucket = redisson.getBucket(key);
        return googleAuthRBucket.get();
    }

    /**
     * will insert once generate a ga
     */
    public void putGenerateGoogleAuth(String token,
                                      String secret) {
        Long expireAt = System.currentTimeMillis() + googleAuthExpireAfter * 1000;
        String key = UcRedisKeys.getUcGenerateGoogleAuthKeyTemplate(token);
        RBucket<GenGoogleAuth> genGoogleAuthRBucket = redisson.getBucket(key);
        GenGoogleAuth genGoogleAuth =
                GenGoogleAuth.builder()
                        .secret(secret)
                        .expireAt(expireAt)
                        .build();
        genGoogleAuthRBucket.set(genGoogleAuth, googleAuthExpireAfter, TimeUnit.SECONDS);
        log.info("CacheService - putGenerateGoogleAuth - put key: {}, value: {}", key, genGoogleAuth);
    }

    public GenGoogleAuth getGenerateGoogleAuth(String token) {
        String key = UcRedisKeys.getUcGenerateGoogleAuthKeyTemplate(token);
        RBucket<GenGoogleAuth> genGoogleAuthRBucket = redisson.getBucket(key);
        return genGoogleAuthRBucket.get();
    }

}