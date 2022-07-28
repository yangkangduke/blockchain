package com.seeds.uc.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.BindEmailReq;
import com.seeds.uc.dto.request.QRBarCodeReq;
import com.seeds.uc.dto.request.SendCodeReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@Api(tags = "用户相关接口")
public class OpenUserController {
    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private IGoogleAuthService googleAuthService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private FileTemplate template;

    /**
     * 绑定邮箱-发送验证码
     */
    @PostMapping("/bind/email/send")
    @ApiOperation(value = "绑定邮箱-发送验证码", notes = "useType传BIND_EMAIL")
    public GenericDto<Object> bindEmailSend(@Valid @RequestBody SendCodeReq sendReq) {
        return GenericDto.success(ucUserService.bindEmailSend(sendReq));
    }

    /**
     * 绑定邮箱
     * 1.调用/bind/email/send接口发送邮箱验证码
     * 2.调用/bind/email绑定邮箱接口
     */
    @PostMapping("/bind/email")
    @ApiOperation(value = "绑定邮箱",
            notes = "1.调用send/code接口发送邮箱验证码\n" +
                    "2.调用/bind/email绑定邮箱接口")
    public GenericDto<Object> bindEmail(@Valid @RequestBody BindEmailReq bndEmailReq, HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        ucUserService.bindEmail(bndEmailReq, loginUser);
        return GenericDto.success(null);
    }

    /**
     * 生成QRBarcode
     */
    @PostMapping("/ga/QRBarcode")
    @ApiOperation(value = "生成QRBarcode", notes = "生成QRBarcode")
    public GenericDto<String> getQRBarcode(@Valid @RequestBody QRBarCodeReq qrBarCodeReq, HttpServletRequest request) {
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        return GenericDto.success(googleAuthService.getQRBarcode(qrBarCodeReq.getAccount(), qrBarCodeReq.getRemark(), loginUser));
    }

    /**
     * GA验证code
     * 1.调用/ga/QRBarcode生成code后
     * 2.调用/ga/verifyCode验证code
     */
    @PostMapping("/ga/verifyCode")
    @ApiOperation(value = "GA验证code", notes = "GA验证code")
    public GenericDto<Object> verifyCode(@Valid @NotBlank @RequestBody String code, HttpServletRequest request) {
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        googleAuthService.verifyUserCode(loginUser.getUserId(), code);
        return GenericDto.success(null);
    }

    @PostMapping("/test")
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public GenericDto<LoginResp> test(@RequestPart("file") MultipartFile file) throws Exception {
        template.putObject("s3demo", "fileName", file.getInputStream());
        return GenericDto.success(null);
    }

}
