package com.seeds.uc.web.user.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.model.send.dto.request.EmailCodeSendReq;
import com.seeds.uc.model.send.dto.request.EmailCodeVerifyReq;
import com.seeds.uc.model.user.dto.request.RegisterReq;
import com.seeds.uc.model.user.dto.response.LoginResp;
import com.seeds.uc.web.user.service.IUcUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * user table 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-07-09
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UcUserController {
    @Autowired
    private IUcUserService iUcUserService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/register/email-code/send")
    @ApiOperation(value = "发送邮箱验证码", notes = "发送邮箱验证码")
    public GenericDto<Object> sendEmailCode(@Valid @RequestBody EmailCodeSendReq sendReq) {
        iUcUserService.sendEmailCode(sendReq);
        return GenericDto.success(null);
    }

    /**
     * 邮箱验证码校验
     */
    @PostMapping("/register/email-code/verify")
    @ApiOperation(value = "邮箱验证码校验", notes = "邮箱验证码校验")
    public GenericDto<Object> verifyRegisterCode(@Valid @RequestBody EmailCodeVerifyReq verifyReq) {
        iUcUserService.verifyRegisterCode(verifyReq);
        return GenericDto.success(null);
    }

    /**
     * 账号重复性校验
     */
    @GetMapping("/register/account/verify")
    @ApiOperation(value = "账号重复性校验", notes = "账号重复性校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "注册的邮箱账号" ,required = false),
    })
    public GenericDto<Boolean> accountVerify(@Validated @NotBlank String account) {
        return GenericDto.success(iUcUserService.accountVerify(account));
    }

    /**
     * 注册用户
     */
    @PostMapping("/register/create-account")
    @ApiOperation(value = "注册用户", notes = "注册用户")
    public GenericDto<LoginResp> createAccount(@RequestBody RegisterReq registerReq) {
        return GenericDto.success(iUcUserService.createAccount(registerReq));
    }

    /**
     * GA Authentication QR code
     */
    /**
     * GA Authentication 绑定
     */
    /**
     * metamask注册
     */
    /**
     * 用户绑定metamask
     */
    /**
     * metamask绑定账号
     */

}
