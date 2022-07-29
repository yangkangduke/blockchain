package com.seeds.uc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.impl.GoogleAuthServiceImpl;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@Slf4j
@RestController
@Api(tags = "auth相关接口")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private SendCodeService sendCodeService;
    @Autowired
    private IGoogleAuthService googleAuthService;


    /**
     * 注册邮箱账号-发送邮箱验证码
     */
    @PostMapping("/register/emailSend")
    @ApiOperation(value = "注册邮箱账号-发送邮箱验证码", notes = "注册邮箱账号-发送邮箱验证码")
    public GenericDto<Object> registerEmailSend(@Valid @Email @RequestBody String email) {
        ucUserService.registerEmailSend(email);
        return GenericDto.success(null);
    }

    /**
     * 注册邮箱账号
     * 1.调用/register/emailSend 发送邮箱验证码，
     * 2.调用/register/emailAccount 注册邮箱账号
     */
    @PostMapping("/register/emailAccount")
    @ApiOperation(value = "注册邮箱账号",
            notes = "1.调用/register/emailSend 发送邮箱验证码，\n" +
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
        return GenericDto.success(ucUserService.login(loginReq));
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
    public GenericDto<LoginResp> twoFactorCheck(@RequestBody TwoFactorLoginReq loginReq) {
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
        return GenericDto.success(ucUserService.metamaskNonce(metaMaskReq, null));
    }

    /**
     * metamask验证
     * 1.调用/metamask/nonce生成nonce
     * 2.前端根据nonce生成签名信息
     * 3.调用/metamask/verify验证签名信息，验证成功返回token
     *
     * @param
     * @return
     */
    @PostMapping("/metamask/verify")
    @ApiOperation(value = "metamask验证",
            notes = "1.调用/metamask/nonce生成nonce\n" +
                    "2.前端根据nonce生成签名信息\n" +
                    "3.调用/metamask/verify验证签名信息，验证成功返回token")
    public GenericDto<LoginResp> metamaskVerify(@Valid @RequestBody MetaMaskReq metaMaskReq) {
        return GenericDto.success(ucUserService.metamaskVerify(metaMaskReq));
    }

    /**
     * 忘记密码-发送邮件
     *
     * @return
     */
    @PostMapping("/forgotPassword/seedEmail")
    @ApiOperation(value = "忘记密码-发送邮件", notes = "忘记密码-发送邮件")
    public GenericDto<Object> forgotPasswordSeedEmail(@Valid @RequestBody ForgotPasswordReq forgotPasswordReq) {
        ucUserService.forgotPasswordSeedEmail(forgotPasswordReq);
        return GenericDto.success(null);
    }

    /**
     * 忘记密码-验证链接
     *
     * @return
     */
    @GetMapping("/forgotPassword/verifyLink")
    @ApiOperation(value = "忘记密码-验证链接", notes = "忘记密码-验证链接")
    public GenericDto<Object> forgotPasswordVerifyLink(String encode) {
        ucUserService.forgotPasswordVerifyLink(encode);
        return GenericDto.success(null);
    }

    /**
     * 忘记密码-ga验证
     *
     * @return
     */
    @PostMapping("/forgotPassword/gaVerify")
    @ApiOperation(value = "忘记密码-ga验证", notes = "忘记密码-ga验证")
    public GenericDto<Object> forgotPasswordGaVerify(String code,String email) {
        // 通过email找到用户
        UcUser one = ucUserService.getOne(new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, email));
        if (one == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
        }
        if (!googleAuthService.verify(code, one.getGaSecret())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
        }
        return GenericDto.success(null);
    }

    /**
     * 忘记密码-修改密码
     *
     * @return
     */
    @PostMapping("/forgotPassword/changePassword")
    @ApiOperation(value = "忘记密码-修改密码", notes = "忘记密码-修改密码")
    public GenericDto<Object> forgotPasswordChangePassword(ChangePasswordReq changePasswordReq) {
        ucUserService.forgotPasswordVerifyLink(changePasswordReq.getEncode());
        ucUserService.forgotPasswordChangePassword(changePasswordReq);
        return GenericDto.success(null);
    }

//    @PostMapping("email/send")
//    public GenericDto<Object> sendEmailCode(@RequestBody AuthCodeSendReq sendReq, HttpServletRequest request) {
//        log.info("AuthController - sendEmailCode got request: {}", sendReq);
//        if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_REQUEST.contains(sendReq.getUseType())) {
//            // 不需要登陆，请求里带邮箱，如: REGISTER
//            sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
//        } else if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_DB.contains(sendReq.getUseType())) {
//            // 需要token(拒绝登陆后发的用于2FA的token)，邮箱从数据库查, 如：LOGIN
//            sendCodeService.sendEmailWithTokenAndUseType(sendReq.getToken(), sendReq.getUseType());
//        }
////        else {
////            // 需要登陆
////            String loginToken = WebUtil.getTokenFromRequest(request);
////            LoginUser loginUser = cacheService.getUserByToken(loginToken);
////
////            if (StringUtils.isBlank(loginToken) || loginUser == null) {
////                throw new SendAuthCodeException(UcErrorCode.ERR_401_NOT_LOGGED_IN);
////            }
////            // TODO 邮箱需要检查是否已使用
////            if (AuthCodeUseTypeEnum.EMAIL_NEED_LOGIN_READ_REQUEST_SET.contains(sendReq.getUseType())) {
////                sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
////            } else if (AuthCodeUseTypeEnum.EMAIL_NEED_LOGIN_READ_DB_SET.contains(sendReq.getUseType())) {
////                // 需要登陆，但是手机号从DB里读
////                UserDto userDto = userService.getUserByUid(loginUser.getUserId());
////                sendCodeService.sendEmailWithUseType(userDto.getEmail(), sendReq.getUseType());
////            } else {
////                // 没有被录入的类型，直接抛错误，后面再加
////                throw new SendAuthCodeException(UcErrorCode.ERR_502_ILLEGAL_ARGUMENTS);
////            }
////        }
//
//        return GenericDto.success(null);
//    }


}
