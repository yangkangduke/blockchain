package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.OpenNftPublicBackpackCreateUpdateReq;
import com.seeds.game.dto.request.OpenNftPublicBackpackDisReq;
import com.seeds.game.dto.request.OpenNftPublicBackpackPageReq;
import com.seeds.game.dto.request.OpenNftPublicBackpackTakeBackReq;
import com.seeds.game.dto.response.NftPublicBackpackResp;
import com.seeds.game.dto.response.OpenNftPublicBackpackDisResp;
import com.seeds.game.service.INftPublicBackpackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * NFT公共背包  游戏方调用，需要带上 accessKey、signature、timestamp
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */


@Api(tags = "NFT公共背包接口，游戏方调用")
@RestController
@RequestMapping("/nft-backpack")
public class OpenNftPublicBackpackController {

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @PostMapping("page")
    @ApiOperation("获取分页信息")
    public GenericDto<IPage<NftPublicBackpackResp>> queryPage(@Valid @RequestBody OpenNftPublicBackpackPageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.queryPage(req));
    }


    @PostMapping("list")
    @ApiOperation("获取列表信息，不分页")
    public GenericDto<List<NftPublicBackpackResp>> queryList(@Valid @RequestBody OpenNftPublicBackpackPageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.queryList(req));
    }

    @GetMapping("detail")
    @ApiOperation("详细信息")
    public GenericDto<NftPublicBackpackResp> detail(@RequestParam Integer autoId,
                                                    @RequestParam String accessKey,
                                                    @RequestParam String signature,
                                                    @RequestParam Long timestamp) {
        return GenericDto.success(nftPublicBackpackService.detail(autoId));
    }

    @PostMapping("create")
    @ApiOperation("新增")
    public GenericDto<Object> create(@RequestBody @Valid OpenNftPublicBackpackCreateUpdateReq req) {
        nftPublicBackpackService.create(req);
        return GenericDto.success(null);
    }

    @PostMapping("update")
    @ApiOperation("修改")
    public GenericDto<Object> update(@RequestBody @Valid OpenNftPublicBackpackCreateUpdateReq req) {
        nftPublicBackpackService.update(req);
        return GenericDto.success(null);
    }

    @PostMapping("distribute")
    @ApiOperation("分配")
    public GenericDto<OpenNftPublicBackpackDisResp> distribute(@RequestBody @Valid OpenNftPublicBackpackDisReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.distribute(req));
    }

    @PostMapping("take-back")
    @ApiOperation("收回")
    public GenericDto<Object> takeBack(@RequestBody @Valid OpenNftPublicBackpackTakeBackReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        nftPublicBackpackService.takeBack(req);
        return GenericDto.success(null);
    }

    @PostMapping("transfer")
    @ApiOperation("转移")
    public GenericDto<OpenNftPublicBackpackDisResp> transfer(@RequestBody @Valid OpenNftPublicBackpackDisReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.transfer(req));
    }

    @PostMapping("total-price")
    @ApiOperation("获取nft的参考价")
    public GenericDto<BigDecimal> getTotalPrice(@RequestBody List<Long> autoIds) {

        return GenericDto.success(nftPublicBackpackService.getTotalPrice(autoIds));
    }

}
