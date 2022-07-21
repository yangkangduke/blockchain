package com.seeds.gateway.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.gateway.dto.ReactiveDto;
import com.seeds.gateway.service.AuthService;
import com.seeds.uc.constant.UcRedisKeys;
import com.seeds.uc.dto.LoginUser;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Profile("!prd")
@RestController
@RequestMapping("mock")
public class MockController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RedissonClient redissonClient;

    private Long expire = 5 * 60_000L;

    @PostMapping("redis/set")
    Mono<GenericDto<LoginUser>> set(@RequestParam("token") String token, @RequestParam("userId") Long userId) {
        LoginUser loginUser = LoginUser.builder()
                .userId(userId)
                .expireAt(System.currentTimeMillis() + expire).build();
        redissonClient.getBucket(UcRedisKeys.getUcTokenKey(token)).set(loginUser, 5, TimeUnit.MINUTES);
        return ReactiveDto.success(Mono.justOrEmpty(loginUser));
    }

    @GetMapping("redis/get")
    Mono<GenericDto<LoginUser>> get(@RequestParam("token") String token) {
        LoginUser loginUser = redissonClient.<LoginUser>getBucket(UcRedisKeys.getUcTokenKey(token)).get();
        return ReactiveDto.success(Mono.justOrEmpty(loginUser));
    }

}
