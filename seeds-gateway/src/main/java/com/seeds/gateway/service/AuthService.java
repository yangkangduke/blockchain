package com.seeds.gateway.service;

import com.seeds.admin.constant.AdminRedisKeys;
import com.seeds.admin.dto.redis.LoginAdminUser;
import com.seeds.uc.model.cache.constant.UcRedisKeys;
import com.seeds.uc.model.cache.dto.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private RedissonClient redissonClient;

    public LoginUser verify(String token) {
        return redissonClient.<LoginUser>getBucket(UcRedisKeys.getUcTokenKey(token)).get();
    }

    public LoginAdminUser verifyAdmin(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return redissonClient.<LoginAdminUser>getBucket(AdminRedisKeys.getAdminUserTokenKey(token)).get();
    }
}
