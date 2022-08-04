package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.MetaMaskReq;
import com.seeds.uc.dto.request.QRBarCodeReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.enums.UserOperateEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.exceptions.SecuritySettingException;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcSecurityStrategyService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @program: seeds-java
 * @description: 安全策略
 * @author: yk
 * @create: 2022-08-04 11:14
 **/
@RestController
@Api(tags = "安全策略")
public class OpenSecurityController {

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
     * 绑定ga
     * 1.调用/ga/QRBarcode生成gaSecret
     * 2.调用/ga/verifyCode验证code
     */
    @PostMapping("/ga/verifyCode")
    @ApiOperation(value = "绑定ga", notes = "1.调用/ga/QRBarcode生成gaSecret\n" +
            "2.调用/ga/verifyCode验证code")
    public GenericDto<Object> verifyCode(@Valid @NotBlank @RequestBody String code, HttpServletRequest request) {
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        googleAuthService.verifyUserCode(loginUser.getUserId(), code);
        return GenericDto.success(null);
    }

    /**
     * 解除绑定ga
     */
    @PostMapping("/ga/unbind")
    @ApiOperation(value = "解除绑定ga", notes = "解除绑定ga")
    public GenericDto<Object> gaUnbind(@Valid @NotBlank @RequestBody String code, HttpServletRequest request) {
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser ucUser = ucUserService.getById(loginUser.getUserId());
        if (!googleAuthService.verify(code, ucUser.getGaSecret())) {
            throw new SecuritySettingException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }

        // 清空ga信息
        ucUserService.deleteGa(ucUser);
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
     * 修改邮箱
     * @return
     */
    @PutMapping("/change/email")
    @ApiOperation(value = "修改邮箱", notes = "修改邮箱")
    public GenericDto<Object> updateEmail(@Valid @NotBlank @RequestBody String email, HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        // 判断是否有ga
        // 验证email的code
        // 修改邮箱 todo，看之前发邮件取的是哪里的邮箱地址
        return GenericDto.success(ucUserService.updateEmail(email, loginUser));
    }
}
