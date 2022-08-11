package com.seeds.uc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.constant.UcConstant;
import com.seeds.uc.dto.redis.AuthCodeDTO;
import com.seeds.uc.dto.redis.AuthTokenDTO;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.redis.SecurityAuth;
import com.seeds.uc.dto.request.GaReq;
import com.seeds.uc.dto.request.MetamaskBindReq;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.dto.request.UpdateEmailReq;
import com.seeds.uc.dto.request.security.item.EmailSecurityItemReq;
import com.seeds.uc.dto.request.security.item.GaSecurityItemReq;
import com.seeds.uc.dto.response.GoogleAuthResp;
import com.seeds.uc.dto.response.MetamaskAuthResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
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
            notes = "1.调用/security/item/ga/generate获取gaKey " +
                    "2.使用手机扫码添加上gaKey " +
                    "3.调用/code/ga/verify验证验证码并获取gaToken " +
                    "4.调用/email/send发送邮件，参数useType=VERIFY_SETTING_POLICY_BIND_GA " +
                    "5.调用/security/strategy/verify, 参数useType=VERIFY_SETTING_POLICY_BIND_GA, 进行安全验证, 获取到authToken " +
                    "6.调用/security/item/ga/bind绑定ga")
    public GenericDto<Object> bindGA(@RequestBody GaSecurityItemReq securityItemReq) {
        AuthTokenDTO gaAuthToken =
                cacheService.getAuthTokenDetailWithToken(securityItemReq.getGaToken(), ClientAuthTypeEnum.GA);
        if (gaAuthToken == null
                || StringUtils.isBlank(gaAuthToken.getSecret())) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }

        SecurityAuth securityAuth =
                cacheService.getSecurityAuthWithToken(securityItemReq.getAuthToken());
        if (securityAuth == null) {
            throw new SecuritySettingException(UcErrorCodeEnum.ERR_10210_SECURITY_VERIFY_ERROR);
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

    @PostMapping("/ga/unbind")
    @ApiOperation(value = "解除绑定ga", notes = "1.调用auth/email/send 传authType=RESET_GA获取邮箱验证码 " +
            "2.调用/security/item/ga/unbind 解除绑定GA ")
    public GenericDto<Object> gaUnbind(@Valid @RequestBody GaReq gaReq, HttpServletRequest request) {
        String emailCode = gaReq.getEmailCode();
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser ucUser = ucUserService.getById(loginUser.getUserId());
        // 验证邮箱和验证码是否正确
        AuthCodeDTO authCode = cacheService.getAuthCode(ucUser.getEmail(), AuthCodeUseTypeEnum.RESET_GA, ClientAuthTypeEnum.EMAIL);
        if (authCode == null || !emailCode.equals(authCode.getCode())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17000_EMAIL_VERIFICATION_FAILED);
        }

        // 清空ga信息
        ucUserService.deleteGa(ucUser);
        return GenericDto.success(null);
    }


    @PostMapping("/bind/metamask")
    @ApiOperation(value = "绑定metamask",
            notes = "1.调用/auth/metamask/generate-nonce生成nonce " +
                    "2.前端根据nonce生成签名信息 " +
                    "3.调用/code/metamask/verify 验证签名信息，返回authToken " +
                    "4.调用/auth/email/send 参数authType=BIND_METAMASK 获取邮箱验证码 " +
                    "5.调用/security/item/bind/metamask绑定 ")
    public GenericDto<Object> metamaskBind(@Valid @RequestBody MetamaskBindReq bindReq) {
        String authToken = bindReq.getAuthToken();
        String emailCode = bindReq.getEmailCode();
        Long currentUserId = UserContext.getCurrentUserId();
        // 验证authToken
        AuthTokenDTO authTokenDTO = cacheService.getAuthTokenDetailWithToken(authToken, ClientAuthTypeEnum.METAMASK);
        if (authTokenDTO == null) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_16004_METAMASK_VERIFY_EXPIRED);
        }
        UcUser ucUser = ucUserService.getById(currentUserId);
        // 验证emailCode
        AuthCodeDTO authCode = cacheService.getAuthCode(ucUser.getEmail(), AuthCodeUseTypeEnum.BIND_METAMASK, ClientAuthTypeEnum.EMAIL);
        if (authCode == null || !emailCode.equals(authCode.getCode())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17000_EMAIL_VERIFICATION_FAILED);
        }

        // 绑定
        ucUserService.bindMetamask(authTokenDTO, currentUserId);
        return GenericDto.success(null);
    }

    @PostMapping("/email/bind")
    @ApiOperation(value = "绑定邮箱",
            notes = "1.调用/auth/email/send, 参数use_type=BIND_EMAIL, 发送邮箱验证码 " +
                    "2.调用/code/email/verify, 参数use_type=BIND_EMAIL, 进行邮箱的验证, 获取到emailToken " +
                    "3.调用/auth/metamask/generate-nonce 获取随机数 " +
                    "4.调用/code/metamask/verify 验证签名，返回authToken " +
                    "5.调用/security/item/email/bind 绑定邮箱 ")
    public GenericDto<Object> bindEmail(@RequestBody EmailSecurityItemReq securityItemReq) {
        String authToken = securityItemReq.getAuthToken();
        String emailToken = securityItemReq.getEmailToken();
        AuthTokenDTO emailAuthToken = cacheService.getAuthTokenDetailWithToken(emailToken, ClientAuthTypeEnum.EMAIL);
        if (emailAuthToken == null
                || StringUtils.isBlank(emailAuthToken.getAccountName())) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
        }

        // 验证authToken
        AuthTokenDTO authTokenDTO = cacheService.getAuthTokenDetailWithToken(authToken, ClientAuthTypeEnum.METAMASK);
        if (authTokenDTO == null) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_16004_METAMASK_VERIFY_EXPIRED);
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

    @PutMapping("/change/email")
    @ApiOperation(value = "修改邮箱", notes = "1.调用/auth/email/send发送邮箱验证码 参数userType=CHANGE_EMAIL " +
            "2.调用/code/email/verify验证code，参数userType=CHANGE_EMAIL，返回authToken " +
            "3.调用/security/item/change/email修改邮箱 ")
    public GenericDto<Object> updateEmail(@Valid @RequestBody UpdateEmailReq updateEmailReq, HttpServletRequest request) {
        String authToken = updateEmailReq.getAuthToken();
        String gaCode = updateEmailReq.getGaCode();

        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser ucUser = ucUserService.getById(loginUser.getUserId());
        AuthTokenDTO authTokenDTO = cacheService.getAuthTokenDetailWithToken(authToken, ClientAuthTypeEnum.EMAIL);
        if (authTokenDTO == null || authTokenDTO.getAccountName() == null) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
        }
        if (!googleAuthService.verifyUserCode(ucUser.getId(), gaCode)) {
            throw new SecurityItemException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }

        // 修改邮箱
        return GenericDto.success(ucUserService.updateEmail(authTokenDTO.getAccountName(), loginUser));
    }
}
