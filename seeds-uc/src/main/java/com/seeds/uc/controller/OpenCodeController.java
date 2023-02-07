package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.redis.GenGoogleAuth;
import com.seeds.uc.dto.request.AuthCodeVerifyReq;
import com.seeds.uc.dto.request.GoogleAuthVerifyReq;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.dto.request.PhantomVerifyReq;
import com.seeds.uc.dto.response.TokenResp;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.SecurityItemException;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yk
 * @date 2022/8/6
 */
@Slf4j
@RestController
@Api(tags = "code验证")
@RequestMapping("/code")
public class OpenCodeController {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private IUcUserService userService;
    @Autowired
    private SendCodeService sendCodeService;
    @Autowired
    private IGoogleAuthService googleAuthService;



    @PostMapping("/ga/verify")
    @ApiOperation(value = "ga验证", notes = "ga验证")
    public GenericDto<TokenResp> verifyGoogleAuth(HttpServletRequest request, @RequestBody GoogleAuthVerifyReq authVerifyReq) {
        // 用uc token 拿用户的登录名
        String token = WebUtil.getTokenFromRequest(request);
        GenGoogleAuth genGoogleAuth = cacheService.getGenerateGoogleAuth(token);
        if (genGoogleAuth != null
                && StringUtils.isNotBlank(genGoogleAuth.getSecret())
                && googleAuthService.verify(authVerifyReq.getGaCode(), genGoogleAuth.getSecret())) {
            String secret = genGoogleAuth.getSecret();
            String authToken = RandomUtil.genRandomToken(UserContext.getCurrentUserId().toString());
            cacheService.putAuthToken(authToken, secret, null, ClientAuthTypeEnum.GA);
            return GenericDto.success(
                    TokenResp.builder()
                            .token(authToken)
                            .build());
        }
        throw new SecurityItemException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
    }

    @ApiOperation(value = "email验证", notes = "email验证")
    @PostMapping("/email/verify")
    public GenericDto<TokenResp> verifyEmailCode(@RequestBody AuthCodeVerifyReq verifyReq) {
        log.info("OpenCodeController - verifyEmailCode got request: {}", verifyReq);
        String token =
                sendCodeService.verifyEmailWithUseType(
                        verifyReq.getEmail(),
                        verifyReq.getEmailCode(),
                        verifyReq.getUseType());
        return GenericDto.success(
                TokenResp.builder()
                        .token(token)
                        .build());
    }

    @ApiOperation(value = "metamask验证", notes = "metamask验证")
    @PostMapping("/metamask/verify")
    public GenericDto<TokenResp> verifyMetamask(@RequestBody MetamaskVerifyReq verifyReq) {
        log.info("OpenCodeController - verifyMetamask got request: {}", verifyReq);
        userService.verifyMetamask(verifyReq);

        String authToken = RandomUtil.genRandomToken(UserContext.getCurrentUserId().toString());
        cacheService.putAuthToken(authToken, verifyReq.getMessage(), verifyReq.getPublicAddress(), ClientAuthTypeEnum.METAMASK);
        return GenericDto.success(
                TokenResp.builder()
                        .token(authToken)
                        .build());
    }

    @ApiOperation(value = "phantom验证", notes = "phantom验证")
    @PostMapping("/phantom/verify")
    public GenericDto<TokenResp> verifyPhantom(@RequestBody PhantomVerifyReq verifyReq) {
        log.info("OpenCodeController - verifyPhantom got request: {}", verifyReq);
        userService.verifyPhantom(verifyReq);

        String authToken = RandomUtil.genRandomToken(UserContext.getCurrentUserId().toString());
        cacheService.putAuthToken(authToken, verifyReq.getMessage(), verifyReq.getPublicAddress(), ClientAuthTypeEnum.PHANTOM);
        return GenericDto.success(
                TokenResp.builder()
                        .token(authToken)
                        .build());
    }
}
