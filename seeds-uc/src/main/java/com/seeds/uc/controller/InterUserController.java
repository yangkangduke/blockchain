package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.service.IUcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author raowentong
 * @email antilaw@yahoo.com
 * @date 2020/8/26
 */
@Slf4j
@RestController
@RequestMapping("/uc-internal/user")
public class InterUserController {

    @Autowired
    private IUcUserService ucUserService;

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

    @PostMapping("/get-email-by-ids")
    @Inner
    public GenericDto<Map<Long, String>> getEmailByIds(@RequestBody List<Long> ids) {
        return GenericDto.success(ucUserService.queryEmailByIds(ids));
    }
}