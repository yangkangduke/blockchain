package com.seeds.game.controller;

import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.request.SysNftLockReq;
import com.seeds.admin.dto.request.SysNftUpgradeReq;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
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

}
