package com.seeds.admin.controller;

import com.seeds.admin.dto.request.MetaMaskReq;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.MetaMaskFlagEnum;
import com.seeds.admin.exceptions.InvalidArgumentsException;
import com.seeds.admin.service.AdminCacheService;
import com.seeds.admin.service.SysUserService;
import com.seeds.admin.utils.CryptoUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.WalletUtils;

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

    @PostMapping("/nonce")
    @ApiOperation(value = "metamask获取随机数", notes = "metamask获取随机数")
    public GenericDto<String> metamaskNonce(@Valid @RequestBody MetaMaskReq metaMaskReq) {
        return GenericDto.success(sysUserService.metamaskNonce(metaMaskReq.getPublicAddress(), UserContext.getCurrentAdminUserId()));
    }

    @PostMapping("/bind")
    @ApiOperation(value = "绑定metamask",
            notes = "1.调用/metamask/nonce生成nonce\n" +
                    "2.前端根据nonce生成签名信息\n" +
                    "3.调用/metamask/bind验证签名信息")
    public GenericDto<Boolean> bind(@Valid @RequestBody MetaMaskReq metaMaskReq) {
        String signature = metaMaskReq.getSignature();
        String publicAddress = metaMaskReq.getPublicAddress();
        String message = metaMaskReq.getMessage();
        String[] split = message.split(":");
        Long currentAdminUserId = UserContext.getCurrentAdminUserId();
        SysUserEntity userEntity = sysUserService.getById(currentAdminUserId);
        if (!userEntity.getNonce().equals(split[1]) ) {
            throw new InvalidArgumentsException(AdminErrorCodeEnum.ERR_17004_METAMASK_NONCE_INCORRECT);
        }
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
        return GenericDto.success(sysUserService.updateMetaMask(metaMaskReq, currentAdminUserId));
    }

    /**
     * 解除绑定metamask
     *
     * @param
     * @return
     */
    @PostMapping("/unbind")
    @ApiOperation(value = "解除绑定metamask", notes = "解除绑定metamask")
    public GenericDto<Boolean> unbind() {
        SysUserEntity sysUserEntity = sysUserService.getById(UserContext.getCurrentAdminUserId());
        if (MetaMaskFlagEnum.DISABLE.value() == sysUserEntity.getMetamaskFlag()) {
            throw new InvalidArgumentsException(AdminErrorCodeEnum.ERR_70003_METAMASK_UNBIND_REPEATEDLY);
        }
        // 删除metamask相关信息
        return GenericDto.success(sysUserService.deleteMetaMask(sysUserEntity.getId()));
    }

}
