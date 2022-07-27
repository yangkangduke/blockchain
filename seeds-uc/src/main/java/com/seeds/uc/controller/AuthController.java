package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.LoginUser;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@Api(tags = "auth相关接口")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private CacheService cacheService;


    /**
     * 校验账号
     */
    @PostMapping("/register/check")
    @ApiOperation(value = "校验账号", notes = "校验账号")
    public GenericDto<Object> verifyAccount(@Valid @NotBlank @Email @RequestBody String account) {
        ucUserService.verifyAccount(account);
        return GenericDto.success(null);
    }

    /**
     * 注册账号
     * 1.调用/register/check接口校验账号
     * 2.调用/register/account注册账号，
     * 3.如果传了token就跟metaMask做绑定
     */
    @PostMapping("/register/account")
    @ApiOperation(value = "注册账号",
            notes = "1.调用/register/check接口校验账号\n" +
            "2.调用/register/account注册账号，\n" +
            "3.如果传了token就跟metaMask做绑定")
    public GenericDto<LoginResp> registerAccount(@Valid @RequestBody RegisterReq registerReq, HttpServletRequest request) {
        LoginUser loginUser = null;
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        if (loginToken != null) {
            loginUser = cacheService.getUserByToken(loginToken);
        }
        return GenericDto.success(ucUserService.registerAccount(registerReq, loginUser));
    }

    /**
     * 账号登陆
     *
     * @param accountLoginReq
     * @return
     */
    @PostMapping("/login/account")
    @ApiOperation(value = "账号登陆", notes = "账号登陆")
    public GenericDto<LoginResp> loginAccount(@Valid @RequestBody AccountLoginReq accountLoginReq) {
        return GenericDto.success(ucUserService.loginAccount(accountLoginReq));
    }


    /**
     * metamask获取随机数
     *
     * @param
     * @return
     */
    @PostMapping("/metamask/nonce")
    @ApiOperation(value = "metamask获取随机数", notes = "metamask获取随机数")
    public GenericDto<String> metamaskNonce(@Valid @NotBlank @RequestBody String publicAddress, HttpServletRequest request) {
        LoginUser loginUser = null;
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        if (loginToken != null) {
            loginUser = cacheService.getUserByToken(loginToken);
        }
        return GenericDto.success(ucUserService.metamaskNonce(publicAddress, loginUser));
    }

    /**
     * metamask登陆
     * 1.调用/metamask/nonce生成nonce
     * 2.前端根据nonce生成签名信息
     * 3.调用/login/metamask验证签名信息，验证成功返回token
     * @param
     * @return
     */
    @PostMapping("/login/metamask")
    @ApiOperation(value = "metamask登陆",
            notes ="1.调用/metamask/nonce生成nonce\n" +
            "2.前端根据nonce生成签名信息\n" +
            "3.调用/login/metamask验证签名信息，验证成功返回token")
    public GenericDto<LoginResp> loginMetaMask(@Valid @RequestBody MetaMaskLoginReq loginReq) {
        return GenericDto.success(ucUserService.loginMetaMask(loginReq));
    }

    /**
     * 忘记密码-发送邮件
     * @return
     */
    @PostMapping("/forgotPassword/seedEmail")
    @ApiOperation(value = "忘记密码-发送邮件",notes ="忘记密码-发送邮件")
    public GenericDto<Object> forgotPasswordSeedEmail(@Valid @RequestBody ForgotPasswordReq forgotPasswordReq) {
        ucUserService.forgotPasswordSeedEmail(forgotPasswordReq);
        return GenericDto.success(null);
    }

    /**
     * 忘记密码-验证链接
     * @return
     */
    @GetMapping("/forgotPassword/verifyLink")
    @ApiOperation(value = "忘记密码-验证链接",notes ="忘记密码-验证链接")
    public GenericDto<Object> forgotPasswordVerifyLink(String encode) {
        ucUserService.forgotPasswordVerifyLink(encode);
        return GenericDto.success(null);
    }

    /**
     * 忘记密码-修改密码
     * @return
     */
    @PostMapping("/forgotPassword/changePassword")
    @ApiOperation(value = "忘记密码-修改密码",notes ="忘记密码-修改密码")
    public GenericDto<Object> forgotPasswordChangePassword(ChangePasswordReq changePasswordReq) {
        ucUserService.forgotPasswordVerifyLink(changePasswordReq.getEncode());
        ucUserService.forgotPasswordChangePassword(changePasswordReq);
        return GenericDto.success(null);
    }


}
