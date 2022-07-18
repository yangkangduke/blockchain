package com.seeds.admin.web.auth.service.impl;

import com.seeds.admin.constant.AdminRedisKeys;
import com.seeds.admin.dto.redis.LoginAdminUser;
import com.seeds.admin.web.auth.service.AdminCacheService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 管理后台redis缓存
 *
 * @author hang.yu
 * @date 2022/7/13
 */
@Slf4j
@Service
public class AdminCacheServiceImpl implements AdminCacheService {

    @Autowired
    private RedissonClient redisson;

    @Value("${admin.login.captcha.expire:300}")
    private Integer captchaExpireAfter;

    @Value("${admin.login.redis.expire:259200}")
    private Integer loginExpireAfter;

    @Override
    public String getCaptchaCache(String uuid) {
        String value = null;
        String key = AdminRedisKeys.getAdminLoginCaptchaKeyTemplate(uuid);
        RBucket<String> bucket = redisson.getBucket(key);
        if (bucket != null) {
            value = bucket.get();
            //删除验证码
            bucket.delete();
        }
        return value;
    }

    @Override
    public void putCaptchaCache(String uuid, String value) {
        String key = AdminRedisKeys.getAdminLoginCaptchaKeyTemplate(uuid);
        redisson.getBucket(key).set(
                value,
                captchaExpireAfter,
                TimeUnit.SECONDS);
    }

    @Override
    public Integer putAdminUserWithToken(String token, Long uid) {
        Long expireAt = System.currentTimeMillis() + loginExpireAfter * 1000;
        String key = AdminRedisKeys.getAdminUserTokenKey(token);
        String loginUidKey = AdminRedisKeys.getAdminUserIdKey(uid);
        LoginAdminUser loginUser = LoginAdminUser.builder()
                .userId(uid)
                .expireAt(expireAt)
                .build();

        redisson.getBucket(key).set(loginUser, loginExpireAfter, TimeUnit.SECONDS);

        // set uid -> token
        redisson.getBucket(loginUidKey).set(token, loginExpireAfter, TimeUnit.SECONDS);

        log.info("AdminCacheService - putAdminUserWithToken - put key: {}, value: {}", key, loginUser);
        return loginExpireAfter;
    }

    @Override
    public LoginAdminUser getAdminUserByToken(String token) {
        String key = AdminRedisKeys.getAdminUserTokenKey(token);
        RBucket<LoginAdminUser> value = redisson.getBucket(key);
        return value.get();
    }

    // 用token来remove用户登陆态
    @Override
    public void removeAdminUserByToken(String token) {
        String key = AdminRedisKeys.getAdminUserTokenKey(token);
        RBucket<LoginAdminUser> value = redisson.getBucket(key);
        removeAdminUserLoginUidTokenMapping(value.get().getUserId());
        value.delete();
    }

    @Override
    public void removeAdminUserByUserId(Long userId) {
        String key = AdminRedisKeys.getAdminUserIdKey(userId);
        String token = redisson.<String>getBucket(key).get();
        removeAdminUserByToken(token);
    }

    private void removeAdminUserLoginUidTokenMapping(Long uid) {
        String key = AdminRedisKeys.getAdminUserIdKey(uid);
        RBucket<String> value = redisson.getBucket(key);
        value.delete();
    }
}