package com.seeds.uc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.constant.UcConstant;
import com.seeds.uc.dto.redis.AuthTokenDTO;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.GaReq;
import com.seeds.uc.dto.request.MetaMaskReq;
import com.seeds.uc.dto.request.UpdateEmailReq;
import com.seeds.uc.dto.request.security.item.EmailSecurityItemReq;
import com.seeds.uc.dto.request.security.item.GaSecurityItemReq;
import com.seeds.uc.dto.response.GoogleAuthResp;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.MetamaskAuthResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.enums.UserOperateEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.SecurityItemException;
import com.seeds.uc.exceptions.SecuritySettingException;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcSecurityStrategyService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @program: seeds-java
 * @description: 安全策略
 * @author: yk
 * @create: 2022-08-04 11:14
 **/
@RestController
@Api(tags = "安全策略item")
@RequestMapping("/security/item")
public class OpenSecurityItemController {

    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private IGoogleAuthService googleAuthService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private SendCodeService sendCodeService;
    @Autowired
    private IUcSecurityStrategyService ucSecurityStrategyService;

    @PostMapping("/ga/bind")
    @ApiOperation(value = "绑定ga",
            notes = "1.调用/security/item/ga/generate获取gakey " +
                    "2.手机扫码添加上gakey（secret） " +
                    "3.调用/code/ga/verify验证码的验证并获取gaToken " +
                    "4.调用/security/item/ga/bind绑定ga")
    public GenericDto<Object> bindGA(@RequestBody GaSecurityItemReq securityItemReq) {
        AuthTokenDTO gaAuthToken =
                cacheService.getAuthTokenDetailWithToken(securityItemReq.getGaToken(), ClientAuthTypeEnum.GA);
        if (gaAuthToken == null
                || StringUtils.isBlank(gaAuthToken.getSecret())) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }

        Long uid = UserContext.getCurrentUserId();
        ucUserService.updateById(UcUser.builder()
                        .id(uid)
                        .gaSecret(gaAuthToken.getSecret())
                .build());

        // 绑定之后set 安全项
        ucSecurityStrategyService.save(
                UcSecurityStrategy.builder()
                        .authType(ClientAuthTypeEnum.GA)
                        .createdAt(System.currentTimeMillis())
                        .needAuth(true)
                        .uid(uid)
                        .updatedAt(System.currentTimeMillis())
                        .build());

        return GenericDto.success(null);
    }

    @ApiOperation(value = "生成ga的secret", notes = "生成ga的secret")
    @GetMapping("/ga/generate")
    public GenericDto<GoogleAuthResp> generateGa(HttpServletRequest request) {
        // 用uc token 拿用户的登录名
        String token = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(token);

        String secret = googleAuthService.genGaSecret();
        cacheService.putGenerateGoogleAuth(token, secret);
        return GenericDto.success(
                GoogleAuthResp.builder()
                        .gaKey(secret)
                        .exchangeName(UcConstant.GA_ISSUER)
                        .loginName(loginUser.getLoginName())
                        .build());
    }

    /**
     * 解除绑定ga
     */
    @PostMapping("/ga/unbind")
    @ApiOperation(value = "解除绑定ga", notes = "解除绑定ga")
    public GenericDto<Object> gaUnbind(@Valid @RequestBody GaReq gaReq, HttpServletRequest request) {
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser ucUser = ucUserService.getById(loginUser.getUserId());
        if (!googleAuthService.verify(gaReq.getCode(), ucUser.getGaSecret())) {
            throw new SecuritySettingException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }

        // 清空ga信息
        ucUserService.deleteGa(ucUser);
        return GenericDto.success(null);
    }

    @ApiOperation(value = "生成metamask的nonce", notes = "生成metamask的nonce")
    @PostMapping("/metamask/generate-nonce")
    public GenericDto<MetamaskAuthResp> generateNonce(@Valid @RequestBody MetaMaskReq metaMaskReq) {
        String nonce = RandomUtil.getRandomSalt();
        cacheService.putGenerateMetamaskAuth(metaMaskReq.getPublicAddress(), nonce);
        return GenericDto.success(
                MetamaskAuthResp.builder()
                        .nonce(nonce)
                        .publicAddress(metaMaskReq.getPublicAddress())
                        .build());
    }

    @PostMapping("/bind/metamask")
    @ApiOperation(value = "绑定metamask",
            notes = "1.调用/security/item/metamask/generate-nonce生成nonce\n" +
                    "2.前端根据nonce生成签名信息\n" +
                    "3.调用/security/item/bind/metamask绑定")
    public GenericDto<Object> metamaskVerify(@Valid @RequestBody MetaMaskReq metaMaskReq, HttpServletRequest request) {
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        ucUserService.bindMetamask(metaMaskReq, loginUser.getUserId());
        return GenericDto.success(null);
    }

    @PostMapping("/email/bind")
    @ApiOperation(value = "绑定邮箱",
            notes = "调用方法/auth/email/send, 参数use_type=BIND_EMAIL, 发送邮箱验证码\n" +
                    "调用方法/code/email/verify, 参数use_type=BIND_EMAIL, 进行邮箱的验证, 获取到email_token.\n" +
                    "调用/security/item/email/bind")
    public GenericDto<Object> bindEmail(@RequestBody EmailSecurityItemReq securityItemReq) {
        AuthTokenDTO emailAuthToken =
                cacheService.getAuthTokenDetailWithToken(securityItemReq.getEmailToken(), ClientAuthTypeEnum.EMAIL);
        if (emailAuthToken == null
                || StringUtils.isBlank(emailAuthToken.getAccountName())) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
        }

        String accountName = emailAuthToken.getAccountName();
        Long uid = UserContext.getCurrentUserId();
        // 检查account name 是否已经存在
        UcUser checkUser = ucUserService.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getEmail, accountName));
        if (checkUser != null) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_10061_EMAIL_ALREADY_BEEN_USED);
        }
        String salt = RandomUtil.getRandomSalt();
        String password = PasswordUtil.getPassword(securityItemReq.getPassword(), salt);
        ucUserService.updateById(UcUser.builder()
                        .id(uid)
                        .salt(salt)
                        .password(password)
                        .email(accountName)
                .build());

        // 绑定之后set 安全项
        ucSecurityStrategyService.save(
                UcSecurityStrategy.builder()
                        .authType(ClientAuthTypeEnum.EMAIL)
                        .createdAt(System.currentTimeMillis())
                        .needAuth(true)
                        .uid(uid)
                        .updatedAt(System.currentTimeMillis())
                        .build());

        return GenericDto.success(null);
    }

    /**
     * 修改邮箱
     * @return
     */
    @PutMapping("/change/email")
    @ApiOperation(value = "修改邮箱", notes = "修改邮箱")
    public GenericDto<Object> updateEmail(@Valid @RequestBody UpdateEmailReq updateEmailReq, HttpServletRequest request) {
        String email = updateEmailReq.getEmail();
        String code = updateEmailReq.getCode();
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser ucUser = ucUserService.getById(loginUser.getUserId());
        // 判断是否有ga
        if (ucUserService.verifyGa(loginUser.getUserId())) {
            throw new InvalidArgumentsException("You cannot modify your email address after binding Google authentication");
        }
        // 验证email的code
        sendCodeService.verifyEmailWithUseType(ucUser.getEmail(), code, AuthCodeUseTypeEnum.CHANGE_EMAIL);
        // 修改邮箱
        return GenericDto.success(ucUserService.updateEmail(email, loginUser));
    }
}
