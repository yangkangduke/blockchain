package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.redis.SecurityAuth;
import com.seeds.uc.dto.request.VerifyAuthTokenReq;
import com.seeds.uc.service.impl.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author raowentong
 * @email antilaw@yahoo.com
 * @date 2020/9/3
 */
@Slf4j
@RestController
@RequestMapping("/uc-internal/token/")
public class InterTokenController {
    @Autowired
    CacheService cacheService;

    @PostMapping("verify")
    @Inner
    public GenericDto<Boolean> verifyAuthToken(@RequestBody VerifyAuthTokenReq tokenReq) {
        SecurityAuth securityAuth =
                cacheService.getSecurityAuthWithToken(tokenReq.getAuthToken());
        if (securityAuth != null
                && securityAuth.getUid() != null
                && securityAuth.getUid().equals(tokenReq.getUid())
                && securityAuth.getUseType().equals(tokenReq.getUseType())) {
            return GenericDto.success(true);
        } else {
            return GenericDto.success(false);
        }
    }
}
