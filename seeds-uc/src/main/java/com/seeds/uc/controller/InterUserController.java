package com.seeds.uc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.request.LoginReq;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author raowentong
 * @email antilaw@yahoo.com
 * @date 2020/8/26
 */
@Slf4j
@RestController
@RequestMapping("/uc-internal/user")
public class InterUserController {


    @PostMapping("/metamask/verify-signature")
    public GenericDto<Boolean> metaMaskVerifySignature(@RequestBody MetamaskVerifyReq metamaskReq) {
//        String publicAddress = metamaskReq.getPublicAddress();
//
//        MetamaskUser user = metamaskUserService.queryUserWithPublicAddress(publicAddress);
//        if (!user.getUid().equals(metamaskReq.getUserId())) {
//            log.error("Verify signature failed: userId is not match.  {}, {}, {}", publicAddress, metamaskReq.getUserId(), user.getUid());
//            return GenericDto.success(false);
//        }
//
//        log.info("/metamask/verify-signature auth the user from metamask with public address {}", publicAddress);
//        Integer nonce = cacheService.getMetamaskNonceWithPublicAddress(publicAddress);
//        if (nonce != null) {
//            String nonceStr = String.format(metamaskReq.getMsg(), nonce.toString());
//            boolean validate = CryptoUtils.validate(metamaskReq.getSignature(), nonceStr, publicAddress);
//            return GenericDto.success(validate);
//        } else {
//            log.info("cannot find nonce for address {}, {}, {}", metamaskReq.getPublicAddress(), metamaskReq.getSignature(), metamaskReq.getMsg());
//            return GenericDto.success(false);
//        }
        return GenericDto.success(true);
    }
}