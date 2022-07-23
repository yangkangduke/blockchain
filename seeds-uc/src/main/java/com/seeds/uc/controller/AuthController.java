package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.uc.dto.request.AccountLoginReq;
import com.seeds.uc.dto.request.MetaMaskLoginReq;
import com.seeds.uc.dto.request.RegisterReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.service.IUcUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "auth接口")
public class AuthController {

    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private FileTemplate template;

    /**
     * 校验账号
     */
    @PostMapping("/register/check")
    @ApiOperation(value = "校验账号", notes = "校验账号")
    public GenericDto<Boolean> verifyAccount(@Valid @NotBlank @Email @RequestBody String account) {
        return GenericDto.success(ucUserService.verifyAccount(account));
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
        return GenericDto.success(ucUserService.registerAccount(registerReq, request));
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
        return GenericDto.success(ucUserService.metamaskNonce(publicAddress, request));
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
    public GenericDto<LoginResp> loginMetaMask(@Valid @RequestBody MetaMaskLoginReq loginReq, HttpServletRequest request) {
        return GenericDto.success(ucUserService.loginMetaMask(loginReq, request));
    }

    @PostMapping("/test")
    @ApiOperation(value = "文件上传", notes ="文件上传")
    public GenericDto<LoginResp> test(@ApiParam(value = "attach", required = true) MultipartFile file) throws Exception {
        template.putObject("s3demo", "fileName", file.getInputStream());
        return GenericDto.success(null);
    }
}
