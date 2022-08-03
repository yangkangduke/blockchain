package com.seeds.uc.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.MetaMaskReq;
import com.seeds.uc.dto.request.QRBarCodeReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.ProfileResp;
import com.seeds.uc.enums.UserOperateEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        if (!qrBarCodeReq.getAccount().equals(loginUser.getLoginName())) {
            throw new InvalidArgumentsException("Account is incorrect");
        }
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
     * 绑定metamask-获取随机数
     *
     * @param
     * @return
     */
    @PostMapping("/bindMetamask/nonce")
    @ApiOperation(value = "绑定metamask-获取随机数", notes = "绑定metamask-获取随机数")
    public GenericDto<String> metamaskNonce(@Valid @RequestBody MetaMaskReq metaMaskReq, HttpServletRequest request ) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser user = ucUserService.getById(loginUser.getUserId());
        metaMaskReq.setOperateEnum(UserOperateEnum.BIND);
        return GenericDto.success(ucUserService.metamaskNonce(metaMaskReq, user));
    }

    /**
     * 绑定metamask-验证
     * 1.调用/bindMetamask/nonce生成nonce
     * 2.前端根据nonce生成签名信息
     * 3.调用/bindMetamask/verify验证签名信息，验证成功返回token
     *
     * @param
     * @return
     */

    @PostMapping("/bindMetamask/verify")
    @ApiOperation(value = "绑定metamask-验证",
            notes = "1.调用/bindMetamask/nonce生成nonce\n" +
                    "2.前端根据nonce生成签名信息\n" +
                    "3.调用/bindMetamask/verify验证签名信息，验证成功返回token")
    public GenericDto<LoginResp> metamaskVerify(@Valid @RequestBody MetaMaskReq metaMaskReq) {
        return GenericDto.success(ucUserService.metamaskVerify(metaMaskReq));
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/myProfile")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public GenericDto<ProfileResp> getMyProfile(HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        return GenericDto.success(ucUserService.getMyProfile(loginUser));
    }


}
