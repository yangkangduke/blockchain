package com.seeds.uc.web.user.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.model.cache.dto.LoginUser;
import com.seeds.uc.model.send.dto.request.BndEmailReq;
import com.seeds.uc.model.send.dto.request.EmailCodeSendReq;
import com.seeds.uc.util.WebUtil;
import com.seeds.uc.web.cache.service.CacheService;
import com.seeds.uc.web.user.service.IGoogleAuthService;
import com.seeds.uc.web.user.service.IUcUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * user table 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-07-09
 */
@RestController
@RequestMapping("/uc-internal/user")
@Api(tags = "用户internal相关接口")
public class InterUserController {
    @Autowired
    private IUcUserService iUcUserService;
    @Autowired
    private IGoogleAuthService igoogleAuthService;
    @Autowired
    private CacheService cacheService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/bindEmail/emailCode/send")
    @ApiOperation(value = "发送邮箱验证码", notes = "发送邮箱验证码")
    public GenericDto<Boolean> sendEmailCode(@Valid @RequestBody EmailCodeSendReq sendReq) {
        return GenericDto.success(iUcUserService.sendEmailCode(sendReq));
    }

    /**
     * 绑定邮箱
     */
    @PostMapping("/bindEmail")
    @ApiOperation(value = "绑定邮箱", notes = "绑定邮箱")
    public GenericDto<Boolean> bindEmail(@Valid @RequestBody BndEmailReq bndEmailReq, HttpServletRequest request) {
        return GenericDto.success(iUcUserService.bindEmail(bndEmailReq, request));
    }

    /**
     * GA Authentication生成QRBarcode
     */
    @PostMapping("/getQRBarcode")
    @ApiOperation(value = "GA Authentication生成QRBarcode", notes = "GA Authentication生成QRBarcode")
    public GenericDto<String> getQRBarcode(String account, String remark, HttpServletRequest request) {
        return GenericDto.success(igoogleAuthService.getQRBarcode(account, remark, request));
    }

    /**
     * GA Authentication 验证
     */
    @PostMapping("/verifyUserCode")
    @ApiOperation(value = "GA Authentication 验证", notes = "GA Authentication 验证")
    public GenericDto<Boolean> verifyUserCode(String userInputCode, HttpServletRequest request) {
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUser loginUser = cacheService.getUserByToken(loginToken);
        return GenericDto.success(igoogleAuthService.verifyUserCode(loginUser.getUserId(), userInputCode));
    }


}
