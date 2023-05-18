package com.seeds.uc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.AllUserReq;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.dto.response.UserRegistrationResp;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private CacheService cacheService;

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

    @PostMapping("/all-user")
    @ApiOperation("获取所有用户信息")
    @Inner
    public GenericDto<Page<UcUserResp>> getAllUser(@RequestBody AllUserReq allUserReq) {
        Page page = new Page();
        page.setCurrent(allUserReq.getCurrent());
        page.setSize(allUserReq.getSize());
        return GenericDto.success(ucUserService.getAllUser(page, allUserReq));
    }

    @PostMapping("/user-list")
    @ApiOperation("获取用户信息列表")
    @Inner
    public GenericDto<List<UcUserResp>> getUserList(@RequestBody List<Long> ids) {
        return GenericDto.success(ucUserService.getUserList(ids));
    }

    @ApiOperation("获取用户注册情况")
    @Inner
    @GetMapping("/user-registration")
    GenericDto<UserRegistrationResp> getUserRegistration() {
        return GenericDto.success(ucUserService.getUserRegistration());
    }

    @ApiOperation("根据用户id获取用户地址")
    @Inner
    @GetMapping("/get-public-address")
    GenericDto<String> getPublicAddress(@RequestParam Long id) {
        return GenericDto.success(ucUserService.getPublicAddress(id));
    }

    @ApiOperation("根据用户钱包地址获取用户信息")
    @Inner
    @GetMapping("/get-by-public-address")
    GenericDto<UcUserResp> getByPublicAddress(@RequestParam String publicAddress) {
        return GenericDto.success(ucUserService.getByPublicAddress(publicAddress));
    }

    @ApiOperation("根据用户钱包地址获取用户信息")
    @Inner
    @PostMapping("/map-by-public-address")
    GenericDto<Map<String, UcUser>> mapByPublicAddress(@RequestBody Set<String> publicAddress) {
        return GenericDto.success(ucUserService.mapByPublicAddress(publicAddress));
    }

    @ApiOperation("通过邮箱获取用户")
    @Inner
    @GetMapping("/get-by-email")
    GenericDto<UcUser> getByEmail(@RequestParam String email) {
        return GenericDto.success(ucUserService.getByEmail(email));
    }

}