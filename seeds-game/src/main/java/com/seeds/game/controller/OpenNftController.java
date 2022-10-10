package com.seeds.game.controller;

import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.request.SysNftAddReq;
import com.seeds.admin.dto.request.SysNftLockReq;
import com.seeds.admin.dto.request.SysNftUpgradeReq;
import com.seeds.admin.enums.SysOwnerTypeEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.RequestSource;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.feign.RemoteNFTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 提供外部调用的NFT相关接口
 * @author hang.yu
 * @date 2022/10/08
 */
@Slf4j
@Api(tags = "NFT外部调用授权")
@RestController
@RequestMapping("/nft")
public class OpenNftController {

    @Autowired
    private RemoteNftService remoteNftService;

    @Autowired
    private RemoteNFTService remoteNFTService;

    @PostMapping("create")
    @ApiOperation("NFT创建")
    public GenericDto<Long> create(@RequestPart("image") MultipartFile image, @RequestParam String metaData) {
        SysNftAddReq req = JSONUtil.toBean(metaData, SysNftAddReq.class);
        req.setOwnerId(UserContext.getCurrentUserId());
        req.setOwnerName(UserContext.getCurrentUserName());
        req.setOwnerType(SysOwnerTypeEnum.UC_USER.getCode());
        return remoteNftService.create(image, JSONUtil.toJsonStr(req));
    }

    @PostMapping("upgrade")
    @ApiOperation("NFT升级")
    public GenericDto<Long> upgrade(@RequestPart("image") MultipartFile image, @RequestParam String data) {
        SysNftUpgradeReq req = JSONUtil.toBean(data, SysNftUpgradeReq.class);
        req.setUserId(UserContext.getCurrentUserId());
        return remoteNftService.upgrade(image, JSONUtil.toJsonStr(req));
    }

    @PostMapping("lock")
    @ApiOperation("NFT锁定")
    public GenericDto<Object> lock(@Valid @RequestBody SysNftLockReq req) {
        return remoteNftService.lock(req);
    }

    @PostMapping("buy")
    @ApiOperation("NFT购买")
    public GenericDto<Object> buy(@Valid @RequestBody NFTBuyReq req) {
        req.setSource(RequestSource.GAME);
        return remoteNFTService.buyNFT(req);
    }

}
