package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.redis.GenGoogleAuth;
import com.seeds.uc.dto.request.AuthCodeVerifyReq;
import com.seeds.uc.dto.request.GoogleAuthVerifyReq;
import com.seeds.uc.dto.response.TokenResp;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.SecurityItemException;
import com.seeds.uc.service.CacheService;
import com.seeds.uc.service.GoogleAuthService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.UserService;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Slf4j
@RestController
@RequestMapping("/uc/code")
public class OpenCodeController {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;
    @Autowired
    private SendCodeService sendCodeService;
    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("sms/verify")
    public GenericDto<TokenResp> verifySmsCode(@RequestBody AuthCodeVerifyReq verifyReq) {
        log.info("OpenCodeController - verifySmsCode got request: {}", verifyReq);

        String token =
                sendCodeService.verifySmsWithUseType(
                        verifyReq.getCountryCode(),
                        verifyReq.getPhone(),
                        verifyReq.getSmsCode(),
                        verifyReq.getUseType());
        return GenericDto.success(
                TokenResp.builder()
                        .token(token)
                        .build());
    }

    @PostMapping("email/verify")
    public GenericDto<Object> verifyEmailCode(@RequestBody AuthCodeVerifyReq verifyReq) {
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

    /**
     * TODO 考虑使用场景 usetype的影响
     *
     * @return
     */
    @PostMapping("ga/verify")
    public GenericDto<TokenResp> verifyGoogleAuth(HttpServletRequest request,
                                                  @RequestBody GoogleAuthVerifyReq authVerifyReq) {
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
        throw new SecurityItemException(UcErrorCode.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
    }
}
