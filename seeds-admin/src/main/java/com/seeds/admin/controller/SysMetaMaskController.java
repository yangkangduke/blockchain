package com.seeds.admin.controller;

import com.seeds.admin.dto.redis.LoginAdminUser;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.MetaMaskFlagEnum;
import com.seeds.admin.exceptions.InvalidArgumentsException;
import com.seeds.admin.service.AdminCacheService;
import com.seeds.admin.service.SysUserService;
import com.seeds.admin.utils.CryptoUtils;
import com.seeds.admin.utils.WebUtil;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.LoginReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.UserInfoResp;
import com.seeds.uc.feign.RemoteUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.WalletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * metamask
 *
 * @author yk
 * @date 2022/7/13
 */
@Slf4j
@Api(tags = "小狐狸钱包")
@RestController
@RequestMapping("/metamask")
public class SysMetaMaskController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AdminCacheService adminCacheService;
    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * metamask获取随机数
     *
     * @param
     * @return
     */
    @PostMapping("/nonce")
    @ApiOperation(value = "metamask获取随机数", notes = "metamask获取随机数")
    public GenericDto<String> metamaskNonce(@Valid @RequestBody MetaMaskReq metaMaskReq, HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginAdminUser loginAdminUser = adminCacheService.getAdminUserByToken(loginToken);

        return GenericDto.success(sysUserService.metamaskNonce(metaMaskReq.getPublicAddress(), loginAdminUser.getUserId()));
    }

    /**
     * 绑定metamask
     * 1.调用/metamask/nonce生成nonce
     * 2.前端根据nonce生成签名信息
     * 3.调用/metamask/bind验证签名信息
     *
     * @param
     * @return
     */
    @PostMapping("/bind")
    @ApiOperation(value = "绑定metamask",
            notes = "1.调用/metamask/nonce生成nonce\n" +
                    "2.前端根据nonce生成签名信息\n" +
                    "3.调用/metamask/bind验证签名信息")
    public GenericDto<Boolean> bind(@Valid @RequestBody MetaMaskReq metaMaskReq, HttpServletRequest request) {
        String signature = metaMaskReq.getSignature();
        String publicAddress = metaMaskReq.getPublicAddress();
        String message = metaMaskReq.getMessage();
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginAdminUser loginAdminUser = adminCacheService.getAdminUserByToken(loginToken);
        // 地址合法性校验
        if (!WalletUtils.isValidAddress(metaMaskReq.getPublicAddress())) {
            // 不合法直接返回错误
            throw new InvalidArgumentsException(AdminErrorCodeEnum.ERR_70001_METAMASK_ADDRESS);
        }
        // 校验签名信息
        try{
            if (!CryptoUtils.validate(signature, message, publicAddress)) {
                throw new InvalidArgumentsException(AdminErrorCodeEnum.ERR_70002_METAMASK_SIGNATURE);
            }
        } catch (Exception e) {
            throw new InvalidArgumentsException(AdminErrorCodeEnum.ERR_70002_METAMASK_SIGNATURE);
        }
        // 更新
        return GenericDto.success(sysUserService.updateMetaMask(loginAdminUser.getUserId()));
    }

    /**
     * 解除绑定metamask
     *
     * @param
     * @return
     */
    @PostMapping("/unbind")
    @ApiOperation(value = "解除绑定metamask", notes = "解除绑定metamask")
    public GenericDto<Boolean> unbind(HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginAdminUser loginAdminUser = adminCacheService.getAdminUserByToken(loginToken);
        SysUserEntity sysUserEntity = sysUserService.getById(loginAdminUser.getUserId());
        if (MetaMaskFlagEnum.DISABLE.value() == sysUserEntity.getMetamaskFlag()) {
            throw new InvalidArgumentsException(AdminErrorCodeEnum.ERR_70003_METAMASK_UNBIND_REPEATEDLY);
        }
        // 删除metamask相关信息
        return GenericDto.success(sysUserService.deleteMetaMask(sysUserEntity.getId()));
    }

    @PostMapping("/test")
    @ApiOperation(value = "test", notes = "test")
    public GenericDto<Object> test(@Valid @RequestBody LoginReq loginReq) {
        GenericDto<LoginResp> login = remoteUserService.login(loginReq);
        return GenericDto.success(login);
    }

}
