package com.seeds.gateway.service;

import com.seeds.uc.constant.UcRedisKeys;
import com.seeds.uc.dto.redis.LoginUser;
import lombok.extern.slf4j.Slf4j;
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

}
