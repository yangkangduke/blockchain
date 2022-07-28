package com.seeds.gateway.service;

import com.seeds.admin.constant.AdminRedisKeys;
import com.seeds.admin.dto.redis.LoginAdminUser;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.dto.LoginUserDTO;
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

    public LoginUserDTO verify(String token) {
        return redissonClient.<LoginUserDTO>getBucket(UcRedisKeysConstant.getUcTokenKey(token)).get();
    }

    public LoginAdminUser verifyAdmin(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return redissonClient.<LoginAdminUser>getBucket(AdminRedisKeys.getAdminUserTokenKey(token)).get();
    }
}
