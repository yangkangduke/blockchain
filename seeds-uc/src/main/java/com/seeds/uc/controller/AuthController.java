package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.LoginReq;
import com.seeds.uc.dto.request.RegisterReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUcUserService iUcUserService;
    @Autowired
    private IGoogleAuthService igoogleAuthService;

    /**
     * 账号重复性校验
     */
    @GetMapping("/register/account/verify")
    @ApiOperation(value = "账号重复性校验", notes = "账号重复性校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "注册的邮箱账号", required = true),
    })
    public GenericDto<Boolean> accountVerify(@Valid @NotBlank @Email String account) {
        return GenericDto.success(iUcUserService.accountVerify(account));
    }

    /**
     * 注册账号用户
     */
    @PostMapping("/register/createAccount")
    @ApiOperation(value = "注册账号用户", notes = "注册账号用户")
    public GenericDto<LoginResp> createAccount(@Valid @RequestBody RegisterReq registerReq, HttpServletRequest request) {
        return GenericDto.success(iUcUserService.createAccount(registerReq, request));
    }

    /**
     * 账号登陆
     *
     * @param loginReq
     * @return
     */
    @PostMapping("/login/toEmailAccount")
    @ApiOperation(value = "账号登陆", notes = "账号登陆")
    public GenericDto<LoginResp> loginToEmailAccount(@Valid @RequestBody LoginReq loginReq) {
        return GenericDto.success(iUcUserService.loginToEmailAccount(loginReq));
    }


    /**
     * metamask登陆获取随机数
     *
     * @param
     * @return
     */
    @PostMapping("/login/toMetamask/nonce")
    @ApiOperation(value = "metamask登陆-获取随机数", notes = "metamask-登陆获取随机数")
    public GenericDto<String> loginToMetamaskNonce(String publicAddress, HttpServletRequest request) {
        return GenericDto.success(iUcUserService.loginToMetamaskNonce(publicAddress, request));
    }

    /**
     * metamask登陆
     *
     * @param
     * @return
     */
    @PostMapping("/login/toMetamask")
    @ApiOperation(value = "metamask登陆", notes = "metamask登陆")
    public GenericDto<LoginResp> loginToMetamask(@Valid @NotBlank String publicAddress, @NotBlank String signature, String message, HttpServletRequest request) {
        return GenericDto.success(iUcUserService.loginToMetamask(publicAddress, signature, message, request));
    }
}
