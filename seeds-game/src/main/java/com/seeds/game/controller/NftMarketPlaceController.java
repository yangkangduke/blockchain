package com.seeds.game.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.service.NftMarketPlaceService;
import com.seeds.game.service.INftPublicBackpackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "NFT市场")
@RestController
@RequestMapping("/nft-marketplace")
public class NftMarketPlaceController {

    @Autowired
    private NftMarketPlaceService nftMarketPlaceService;

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @GetMapping("detail/{id}")
    @ApiOperation("详细信息")
    public GenericDto<NftMarketPlaceDetailResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(nftMarketPlaceService.detail(id));
    }

    @PostMapping("make-offer")
    @ApiOperation("拍卖出价")
    public GenericDto<Object> makeOffer() {
        return null;
    }

    @PostMapping("buy-success")
    @ApiOperation("购买成功")
    public GenericDto<Object> buySuccess(@Valid @RequestBody NftBuySuccessReq req) {
        nftMarketPlaceService.buySuccess(req);
        return GenericDto.success(null);
    }

    @PostMapping("page-skin")
    @ApiOperation("获取皮肤分页信息")
    public GenericDto<IPage<NftMarketPlaceSkinResp>>skinQueryPage(){
        return null;
    }

    @PostMapping("page-equip")
    @ApiOperation("获取装备分页信息")
    public GenericDto<IPage<NftMarketPlaceEqiupmentResp>>equipQueryPage(){
        return null;
    }
}
