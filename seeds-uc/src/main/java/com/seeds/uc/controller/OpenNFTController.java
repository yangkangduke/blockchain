package com.seeds.uc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysNftPageReq;
import com.seeds.admin.dto.request.UcSwitchReq;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * NFT交易 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-08-19
 */
@RestController
@RequestMapping("/nft")
@Api(tags = "NFT交易")
@Slf4j
public class OpenNFTController {

    @Autowired
    private RemoteNftService remoteNftService;

    @PostMapping("/owner-page")
    @ApiOperation(value = "用户拥有分页查询", notes = "用户拥有分页查询")
    public GenericDto<Page<SysNftResp>> ownerPage(@Valid @RequestBody SysNftPageReq query) {
        query.setUserId(UserContext.getCurrentUserId());
        return remoteNftService.ucPage(query);
    }

    @PostMapping("/uc-switch")
    @ApiOperation("uc上架/下架")
    public GenericDto<Object> ucUpOrDown(@Valid @RequestBody UcSwitchReq req) {
        req.setUcUserId(UserContext.getCurrentUserId());
        return remoteNftService.ucUpOrDown(req);
    }

}
