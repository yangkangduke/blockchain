package com.seeds.uc.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.LoginUser;
import com.seeds.uc.dto.request.BindEmailReq;
import com.seeds.uc.dto.request.QRBarCodeReq;
import com.seeds.uc.dto.request.SendCodeReq;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/uc")
@Api(tags = "uc接口")
public class OpenUserController {
    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private IGoogleAuthService googleAuthService;
    @Autowired
    private CacheService cacheService;

    /**
     * 发送验证码
     */
    @PostMapping("send/code")
    @ApiOperation(value = "发送验证码", notes = "绑定邮箱useType传BIND_EMAIL")
    public GenericDto<Object> sendCode(@Valid @RequestBody SendCodeReq sendReq) {
        ucUserService.sendCode(sendReq);
        return GenericDto.success(null);
    }

    /**
     * 绑定邮箱
     * 1.调用send/code接口发送邮箱验证码
     * 2.调用/bind/email绑定邮箱接口
     */
    @PostMapping("/bind/email")
    @ApiOperation(value = "绑定邮箱",
            notes = "1.调用send/code接口发送邮箱验证码\n" +
                    "2.调用/bind/email绑定邮箱接口")
    public GenericDto<Boolean> bindEmail(@Valid @RequestBody BindEmailReq bndEmailReq, HttpServletRequest request) {
        return GenericDto.success(ucUserService.bindEmail(bndEmailReq, request));
    }

    /**
     * 生成QRBarcode
     */
    @PostMapping("/ga/QRBarcode")
    @ApiOperation(value = "生成QRBarcode", notes = "生成QRBarcode")
    public GenericDto<String> getQRBarcode(@Valid @RequestBody QRBarCodeReq qrBarCodeReq, HttpServletRequest request) {
        return GenericDto.success(googleAuthService.getQRBarcode(qrBarCodeReq.getAccount(), qrBarCodeReq.getRemark(), request));
    }

    /**
     * GA验证code
     * 1.调用/ga/QRBarcode生成code后
     * 2.调用/ga/verifyCode验证code
     */
    @PostMapping("/ga/verifyCode")
    @ApiOperation(value = "GA验证code", notes = "GA验证code")
    public GenericDto<Boolean> verifyCode(@Valid @NotBlank @RequestBody String code, HttpServletRequest request) {
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUser loginUser = cacheService.getUserByToken(loginToken);
        return GenericDto.success(googleAuthService.verifyUserCode(loginUser.getUserId(), code));
    }


}
