package com.seeds.uc.web.register.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.EmailCodeSendReq;
import com.seeds.uc.dto.request.EmailCodeVerifyReq;
import com.seeds.uc.web.register.service.RegisterService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Slf4j
@RestController
@RequestMapping("/register")
@ApiModel(value = "注册相关接口")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("email-code/send")
    @ApiOperation(value = "发送邮箱验证码", notes = "发送邮箱验证码")
    public GenericDto<Object> sendEmailCode(@RequestBody EmailCodeSendReq sendReq) {
        registerService.sendEmailCode(sendReq);
        return GenericDto.success(null);
    }

    /**
     * 邮箱验证码校验
     */
    @PostMapping("/email-code/verify")
    @ApiOperation(value = "邮箱验证码校验", notes = "邮箱验证码校验")
    public GenericDto<Object> verifyRegisterCode(@RequestBody EmailCodeVerifyReq verifyReq) {
        registerService.verifyRegisterCode(verifyReq);
        return GenericDto.success(null);
    }

    /**
     * 邮箱账号重复性校验
     */
    /**
     * 创建用户
     */
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