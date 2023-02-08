package com.seeds.uc.service.impl;


import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.response.ProfileInfoResp;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.dto.redis.*;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
* @author yk
 * @date 2020/7/29
 */

@Slf4j
@Getter
@Service
public class CacheService {

    @Value("${uc.sso.redis.expire:259200}")
    private Integer loginExpireAfter;

    @Value("${uc.sso.redis.refresh.expire:604800}")
    private Integer refreshExpireAfter;

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

    // metamask的nonce生成，绑定 的过期时间
    @Value("${uc.metamask.expire:300}")
    private Integer metamaskAuthExpireAfter;

    // phantom的nonce生成，绑定 的过期时间
    @Value("${uc.phantom.expire:300}")
    private Integer phantomkAuthExpireAfter;

    @Value("${uc.profile.info.expire:10}")
    private Integer profileInfoExpireAfter;

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
        log.info("CacheService - put2FAInfoWithTokenAndUserAndAuthType - put key: {}, value: {}", key, authDto);
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

        log.info("CacheService - putUserWithTokenAndLoginName - put key: {}, value: {}", key, loginUser);
    }

    /**
     * key uc:refresh:token:{refreshToken}
     * refreshToken用来重新生成ucToken
     */
    public void putUserRefreshToken(String refreshToken, Long uid, String loginName) {
        Long expireAt = System.currentTimeMillis() + refreshExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcRefreshTokenKey(refreshToken);
        LoginUserDTO loginUser = LoginUserDTO.builder()
                .userId(uid)
                .loginName(loginName)
                .expireAt(expireAt)
                .build();

        redisson.getBucket(key).set(loginUser, refreshExpireAfter, TimeUnit.SECONDS);
        log.info("CacheService - putUserRefreshToken - put key: {}, value: {}", key, loginUser);
    }

    /**
     * key为用户名(手机或邮箱)的cache，用于绑定账户等用到的验证码校验
     */
    public void putAuthCode(String name,
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
                .useType(useType)
                .build();
        redisson.getBucket(authCode.getKey()).set(
                authCode,
                authCodeExpireAfter,
                TimeUnit.SECONDS);
        log.info("CacheService - putAuthCode - put key: {}, value: {}", key, authCode);
    }

    public AuthCodeDTO getAuthCode(String name,
                                   AuthCodeUseTypeEnum useType,
                                   ClientAuthTypeEnum authTypeEnum) {
        String key = UcRedisKeysConstant.getUcAuthCodeKeyWithAuthTypeAndUseType(name, useType, authTypeEnum);
        RBucket<AuthCodeDTO> authCodeDtoRBucket = redisson.getBucket(key);
        return authCodeDtoRBucket.get();
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

    /**
     * key uc:refresh:token:{refreshToken}
     * 如果refresh已过期，会返回null
     */
    public LoginUserDTO getUserByRefreshToken(String refreshToken) {
        String key = UcRedisKeysConstant.getUcRefreshTokenKey(refreshToken);
        RBucket<LoginUserDTO> value = redisson.getBucket(key);
        return value.get();
    }

    // 用uid来remove用户登陆态
    public void removeUserByUid(Long uid) {
        String key = UcRedisKeysConstant.getUcLoginUidKey(uid);
        RBucket<String> value = redisson.getBucket(key);
        String token = value.get();
        removeUserByToken(token);
        value.delete();
    }

    // 用token来remove用户登陆态
    public void removeUserByToken(String token) {
        String key = UcRedisKeysConstant.getUcTokenKey(token);
        RBucket<LoginUserDTO> value = redisson.getBucket(key);
        removeUserLoginUidTokenMapping(value.get().getUserId());
        value.delete();
    }

    private void removeUserLoginUidTokenMapping(Long uid) {
        String key = UcRedisKeysConstant.getUcLoginUidKey(uid);
        RBucket<String> value = redisson.getBucket(key);
        value.delete();
    }

    /**
     * will insert once generate a ga
     */
    public void putGenerateGoogleAuth(String token,
                                      String secret) {
        Long expireAt = System.currentTimeMillis() + googleAuthExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcGenerateGoogleAuthKeyTemplate(token);
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
        String key = UcRedisKeysConstant.getUcGenerateGoogleAuthKeyTemplate(token);
        RBucket<GenGoogleAuth> genGoogleAuthRBucket = redisson.getBucket(key);
        return genGoogleAuthRBucket.get();
    }

    public AuthTokenDTO getAuthTokenDetailWithToken(String token, ClientAuthTypeEnum authTypeEnum) {
        String key = UcRedisKeysConstant.getUcAuthTokenKeyTemplate(token, authTypeEnum);
        RBucket<AuthTokenDTO> googleAuthRBucket = redisson.getBucket(key);
        return googleAuthRBucket.get();
    }

    public void putGenerateMetamaskAuth(String publicAddress,
                                        String nonce) {
        Long expireAt = System.currentTimeMillis() + metamaskAuthExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcGenerateMetamaskAuthKeyTemplate(publicAddress);
        RBucket<GenMetamaskAuth> genMetamaskAuthRBucket = redisson.getBucket(key);
        GenMetamaskAuth genMetamaskAuth =
                GenMetamaskAuth.builder()
                        .nonce(nonce)
                        .expireAt(expireAt)
                        .build();
        genMetamaskAuthRBucket.set(genMetamaskAuth, metamaskAuthExpireAfter, TimeUnit.SECONDS);
        log.info("CacheService - putGenerateMetamaskAuth - put key: {}, value: {}", key, genMetamaskAuth);
    }

    public void putGeneratePhantomAuth(String publicAddress,
                                        String nonce) {
        Long expireAt = System.currentTimeMillis() + phantomkAuthExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcGeneratePhantomAuthKeyTemplate(publicAddress);
        RBucket<GenPhantomAuth> genPhantomAuthRBucket = redisson.getBucket(key);
        GenPhantomAuth genPhantomAuth =
                GenPhantomAuth.builder()
                        .nonce(nonce)
                        .expireAt(expireAt)
                        .build();
        genPhantomAuthRBucket.set(genPhantomAuth, phantomkAuthExpireAfter, TimeUnit.SECONDS);
        log.info("CacheService - putGeneratePhantomAuth - put key: {}, value: {}", key, genPhantomAuth);
    }

    public void putOneDayMarking(String key) {
        key = UcRedisKeysConstant.getOneDayMarkingTemplate(key);
        RBucket<String> bucket = redisson.getBucket(key);
        bucket.set(key, 1, TimeUnit.DAYS);
        log.info("CacheService - putOneDayMarking - put key: {}, value: {}", key, key);
    }

    public String getOneDayMarking(String key) {
        key = UcRedisKeysConstant.getOneDayMarkingTemplate(key);
        RBucket<String> genMetamaskAuthRBucket = redisson.getBucket(key);
        return genMetamaskAuthRBucket.get();
    }

    public void removeOneDayMarking(String key) {
        key = UcRedisKeysConstant.getOneDayMarkingTemplate(key);
        RBucket<String> value = redisson.getBucket(key);
        value.delete();
    }

    public GenMetamaskAuth getGenerateMetamaskAuth(String token) {
        String key = UcRedisKeysConstant.getUcGenerateMetamaskAuthKeyTemplate(token);
        RBucket<GenMetamaskAuth> genMetamaskAuthRBucket = redisson.getBucket(key);
        return genMetamaskAuthRBucket.get();
    }

    public GenPhantomAuth getGeneratePhantomAuth(String token) {
        String key = UcRedisKeysConstant.getUcGeneratePhantomAuthKeyTemplate(token);
        RBucket<GenPhantomAuth> genPhantomAuthRBucket = redisson.getBucket(key);
        return genPhantomAuthRBucket.get();
    }

    /**
     * key 为 authCode 的token，用于账户安全项如metamask， email， ga的改动的策略验证
     */
    public void putSecurityAuthTokenWithUidAndUseType(String token,
                                                      Long uid,
                                                      AuthCodeUseTypeEnum useTypeEnum) {
        Long expireAt = System.currentTimeMillis() + securityAuthTokenExpireAfter * 1000;
        String key = UcRedisKeysConstant.getUcSecurityAuthTokenKeyTemplate(token);
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
        String key = UcRedisKeysConstant.getUcSecurityAuthTokenKeyTemplate(token);
        RBucket<SecurityAuth> securityAuthRBucket = redisson.getBucket(key);
        return securityAuthRBucket.get();
    }

    public String getProfileInfo(String userId, String gameId) {
        String key = UcRedisKeysConstant.getProfileInfoTemplate(userId, gameId);
        RBucket<String> bucket = redisson.getBucket(key);
        return bucket.get();
    }

    public void putProfileInfo(String userId, String gameId, ProfileInfoResp data) {
        String key = UcRedisKeysConstant.getProfileInfoTemplate(userId, gameId);
        RBucket<String> bucket = redisson.getBucket(key);
        data.setExpireTime(System.currentTimeMillis() + profileInfoExpireAfter * 60 * 1000);
        bucket.set(JSONUtil.toJsonStr(data), profileInfoExpireAfter, TimeUnit.DAYS);
    }
}