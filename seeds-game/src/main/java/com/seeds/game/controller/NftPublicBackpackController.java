package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.inner.Inner;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
import com.seeds.game.dto.response.NftPublicBackpackResp;
import com.seeds.game.dto.response.OpenNftPublicBackpackDisResp;
import com.seeds.game.service.INftPublicBackpackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * NFT公共背包  web端调用
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */

@Api(tags = "NFT公共背包接口，web调用")
@RestController
@RequestMapping("/web/nft-backpack")
public class NftPublicBackpackController {

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @PostMapping("page")
    @ApiOperation("获取分页信息")
    public GenericDto<IPage<NftPublicBackpackResp>> create(@Valid @RequestBody NftPublicBackpackPageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.queryPage(req));
    }

    @PostMapping("detail")
    @ApiOperation("详细信息")
    public GenericDto<NftPublicBackpackResp> detail(@RequestParam Long id) {
        return GenericDto.success(nftPublicBackpackService.detail(id));
    }

    @PostMapping("create")
    @ApiOperation("新增")
    public GenericDto<Object> create(@RequestBody @Valid NftPublicBackpackReq req) {
        nftPublicBackpackService.create(req);
        return GenericDto.success(null);
    }

    @PostMapping("update")
    @ApiOperation("修改")
    public GenericDto<Object> update(@RequestBody @Valid NftPublicBackpackReq req) {
        nftPublicBackpackService.update(req);
        return GenericDto.success(null);
    }

    @PostMapping("distribute")
    @ApiOperation("分配")
    public GenericDto<OpenNftPublicBackpackDisResp> distribute(@RequestBody @Valid NftPublicBackpackDisReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.distribute(req));
    }

    @PostMapping("take-back")
    @ApiOperation("收回")
    public GenericDto<Object> takeBack(@RequestBody @Valid NftPublicBackpackTakeBackReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        nftPublicBackpackService.takeBack(req);
        return GenericDto.success(null);
    }

    @PostMapping("transfer")
    @ApiOperation("转移")
    public GenericDto<OpenNftPublicBackpackDisResp> transfer(@RequestBody @Valid NftPublicBackpackDisReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.transfer(req));
    }

}
