package com.seeds.uc.web.cache.service;


import com.seeds.uc.model.cache.constant.UcRedisKeys;
import com.seeds.uc.model.cache.dto.AuthCode;
import com.seeds.uc.model.cache.dto.AuthToken;
import com.seeds.uc.model.cache.dto.LoginUser;
import com.seeds.uc.model.send.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.model.user.enums.ClientAuthTypeEnum;
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

}