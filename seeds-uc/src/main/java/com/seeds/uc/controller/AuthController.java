package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.service.IUcUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@Slf4j
@RestController
@Api(tags = "auth相关接口")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUcUserService ucUserService;


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
        return GenericDto.success(ucUserService.metamaskNonce(metaMaskReq));
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


}
