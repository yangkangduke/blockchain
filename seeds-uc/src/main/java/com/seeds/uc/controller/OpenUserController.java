package com.seeds.uc.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.MetaMaskReq;
import com.seeds.uc.dto.request.QRBarCodeReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.model.UcUser;
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

    /**
     * metamask获取随机数
     *
     * @param
     * @return
     */
    @PostMapping("/metamask/nonce")
    @ApiOperation(value = "metamask获取随机数", notes = "metamask获取随机数")
    public GenericDto<String> metamaskNonce(@Valid @RequestBody MetaMaskReq metaMaskReq, HttpServletRequest request ) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser byId = ucUserService.getById(loginUser.getUserId());
        return GenericDto.success(ucUserService.metamaskNonce(metaMaskReq, byId));
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


}
