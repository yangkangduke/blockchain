package com.seeds.uc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.enums.UserOperateEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.SendAuthCodeException;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@Api(tags = "auth")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private SendCodeService sendCodeService;
    @Autowired
    private IGoogleAuthService googleAuthService;
    @Autowired
    private CacheService cacheService;


    /**
     * 注册邮箱账号
     * 1.调用/email/send 发送邮箱验证码，
     * 2.调用/register/emailAccount 注册邮箱账号
     */
    @PostMapping("/register/emailAccount")
    @ApiOperation(value = "注册邮箱账号",
            notes = "1.调用/email/send 发送邮箱验证码，\n" +
                    "2.调用/register/emailAccount 注册邮箱账号")
    public GenericDto<LoginResp> registerEmailAccount(@Valid @RequestBody RegisterReq registerReq) {
        return GenericDto.success(ucUserService.registerEmailAccount(registerReq));
    }

    /**
     * 账号登陆
     * 1.调用/login 返回token
     * 2.调用/2fa/login 返回ucToken
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "账号登陆", notes = "1.调用/login 返回token\n" +
            "2.调用/2fa/login 返回ucToken")
    public GenericDto<LoginResp> login(@Valid @RequestBody LoginReq loginReq) {
        LoginResp login = ucUserService.login(loginReq);
        if (login.getUcToken() == null) {
            return GenericDto.failure(UcErrorCodeEnum.ERR_10070_PLEASE_ENTER_2FA.getDescEn(), UcErrorCodeEnum.ERR_10070_PLEASE_ENTER_2FA.getCode(), login);
        }
        return GenericDto.success(login);
    }

    /**
     * 2fa登陆
     *1.调用/login 返回token
     *2.调用/2fa/login 返回ucToken
     * @param loginReq
     * @return
     */
    @PostMapping("/2fa/login")
    @ApiOperation(value = "2fa登陆", notes = "1.调用/login 返回token\n" +
            "2.调用/2fa/login 返回ucToken")
    public GenericDto<LoginResp> twoFactorCheck(@Valid @RequestBody TwoFactorLoginReq loginReq) {
        return GenericDto.success(ucUserService.twoFactorCheck(loginReq));

    }


    /**
     * metamask获取随机数
     *
     * @param
     * @return
     */
    @PostMapping("/metamask/nonce")
    @ApiOperation(value = "metamask获取随机数", notes = "metamask获取随机数")
    public GenericDto<String> metamaskNonce(@Valid @RequestBody MetaMaskReq metaMaskReq ) {
        UcUser ucUser = ucUserService.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, metaMaskReq.getPublicAddress()));
        if (ucUser == null) {
            metaMaskReq.setOperateEnum(UserOperateEnum.REGISTER);
        } else {
            metaMaskReq.setOperateEnum(UserOperateEnum.LOGIN);
        }
        return GenericDto.success(ucUserService.metamaskNonce(metaMaskReq, ucUser));
    }

    /**
     * metamask登陆
     * 1.调用/metamask/nonce生成nonce
     * 2.前端根据nonce生成签名信息
     * 3.调用/metamask/verify验证签名信息，验证成功返回token
     *
     * @param
     * @return
     */
    @PostMapping("/metamask/verify")
    @ApiOperation(value = "metamask登陆",
            notes = "1.调用/metamask/nonce生成nonce\n" +
                    "2.前端根据nonce生成签名信息\n" +
                    "3.调用/metamask/verify验证签名信息，验证成功返回token")
    public GenericDto<LoginResp> metamaskVerify(@Valid @RequestBody MetaMaskReq metaMaskReq) {
        return GenericDto.success(ucUserService.metamaskVerify(metaMaskReq));
    }

    /**
     * 忘记密码-验证链接
     * 1.调用/send/email 发送邮件链接
     * 2.调用/forgotPassword/verifyLink 验证链接
     * 3.调用/forgotPassword/changePassword 重置密码
     *
     * @return
     */
    @GetMapping("/forgotPassword/verifyLink")
    @ApiOperation(value = "忘记密码-验证链接", notes = "1.调用/send/email 发送邮件链接\n" +
            "2.调用/forgotPassword/verifyLink 验证链接\n" +
            "3.调用/forgotPassword/reset 重置密码")
    public GenericDto<Object> forgotPasswordVerifyLink(@Valid ForgotPasswordVerifyReq forgotPasswordVerifyReq) {
        ucUserService.forgotPasswordVerifyLink(forgotPasswordVerifyReq.getEncode(), forgotPasswordVerifyReq.getAccount());
        return GenericDto.success(null);
    }


    /**
     * 忘记密码-重置密码
     * 1.通过邮件链接code/或者ga验证code
     * 2.调用/forgotPassword/reset 重置密码
     * @return
     */
    @PostMapping("/forgotPassword/reset")
    @ApiOperation(value = "忘记密码-重置密码", notes = "忘记密码-重置密码")
    public GenericDto<Object> forgotPasswordReset(@Valid @RequestBody ResetPasswordReq resetPasswordReq) {
        String code = resetPasswordReq.getCode();
        String account = resetPasswordReq.getAccount();
        ClientAuthTypeEnum authTypeEnum = resetPasswordReq.getAuthTypeEnum();
        if (ClientAuthTypeEnum.EMAIL.equals(authTypeEnum)) {
            ucUserService.forgotPasswordVerifyLink(code, account);
        } else if (ClientAuthTypeEnum.GA.equals(authTypeEnum)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(code);
            if( !isNum.matches() ){
               throw new InvalidArgumentsException("Please enter a number type code");
            }
            UcUser one = ucUserService.getOne(new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, account));
            if (one == null) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
            }
            try {
                if (!googleAuthService.verify(code, one.getGaSecret())) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
                }
            } catch (Exception e) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
            }
        } else {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_504_MISSING_ARGUMENTS);
        }

        ucUserService.forgotPasswordReset(resetPasswordReq);

        return GenericDto.success(null);
    }

    /**
     * 发送邮件
     * @param sendReq
     * @return
     */
    @ApiOperation(value = "发送邮件", notes = "发送邮件")
    @PostMapping("/email/send")
    public GenericDto<Object> sendEmailCode(@Valid @RequestBody AuthCodeSendReq sendReq, HttpServletRequest request) {
        log.info("AuthController - sendEmailCode got request: {}", sendReq);
        // 注册
        if (AuthCodeUseTypeEnum.REGISTER.equals(sendReq.getUseType())) {
            sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
            // 登陆
        } else if (AuthCodeUseTypeEnum.LOGIN.equals(sendReq.getUseType())) {
            sendCodeService.sendEmailWithTokenAndUseType(sendReq.getToken(), sendReq.getUseType());
            // 忘记密码
        }  else if (AuthCodeUseTypeEnum.RESET_PASSWORD.equals(sendReq.getUseType())) {
            sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
        } else {
            // 需要登陆的，从db中读
            String loginToken = WebUtil.getTokenFromRequest(request);
            LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
            UcUser user = ucUserService.getById(loginUser.getUserId());
            // 修改密码
            if (AuthCodeUseTypeEnum.CHANGE_PASSWORD.equals(sendReq.getUseType())) {
                sendCodeService.sendEmailWithUseType(user.getEmail(), sendReq.getUseType());
             // 修改邮箱
            } else if (AuthCodeUseTypeEnum.CHANGE_EMAIL.equals(sendReq.getUseType())) {
                sendCodeService.sendEmailWithUseType(user.getEmail(), sendReq.getUseType());
            } else {
                throw new SendAuthCodeException(UcErrorCodeEnum.ERR_502_ILLEGAL_ARGUMENTS);
            }

        }



//        if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_REQUEST.contains(sendReq.getUseType())) {
//            // 不需要登陆，请求里带邮箱，如: REGISTER
//            sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
//        } else if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_DB.contains(sendReq.getUseType())) {
//            // 需要token(拒绝登陆后发的用于2FA的token)，邮箱从数据库查, 如：LOGIN
//            sendCodeService.sendEmailWithTokenAndUseType(sendReq.getToken(), sendReq.getUseType());
//        }
//        else {
//            // 需要登陆
//            String loginToken = WebUtil.getTokenFromRequest(request);
//            LoginUser loginUser = cacheService.getUserByToken(loginToken);
//
//            if (StringUtils.isBlank(loginToken) || loginUser == null) {
//                throw new SendAuthCodeException(UcErrorCode.ERR_401_NOT_LOGGED_IN);
//            }
//            // TODO 邮箱需要检查是否已使用
//            if (AuthCodeUseTypeEnum.EMAIL_NEED_LOGIN_READ_REQUEST_SET.contains(sendReq.getUseType())) {
//                sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
//            } else if (AuthCodeUseTypeEnum.EMAIL_NEED_LOGIN_READ_DB_SET.contains(sendReq.getUseType())) {
//                // 需要登陆，但是手机号从DB里读
//                UserDto userDto = userService.getUserByUid(loginUser.getUserId());
//                sendCodeService.sendEmailWithUseType(userDto.getEmail(), sendReq.getUseType());
//            } else {
//                // 没有被录入的类型，直接抛错误，后面再加
//                throw new SendAuthCodeException(UcErrorCode.ERR_502_ILLEGAL_ARGUMENTS);
//            }
//        }

        return GenericDto.success(null);
    }


}
