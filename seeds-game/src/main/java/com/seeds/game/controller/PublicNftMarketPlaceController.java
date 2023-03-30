package com.seeds.game.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.NftMarketPlaceDetailViewReq;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlacePropsPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.*;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.response.*;
import com.seeds.game.enums.NftTypeEnum;
import com.seeds.game.service.NftMarketPlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

/**
 * NFT市场外部调用
 * @author dengyang
 * @date 2023/3/27
 */
@Slf4j
@Api(tags = "NFT市场外部调用开放")
@RestController
@RequestMapping("/public/web/nft-marketplace")
public class PublicNftMarketPlaceController {

    @Autowired
    private NftMarketPlaceService nftMarketPlaceService;

    @GetMapping("detail/{id}")
    @ApiOperation("详细信息")
    public GenericDto<NftMarketPlaceDetailResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(nftMarketPlaceService.detail(id));
    }

    @PostMapping("offer-page")
    @ApiOperation("拍卖出价分页")
    public GenericDto<NftOfferResp> offerPage(@Valid @RequestBody NftOfferPageReq req) {
        return GenericDto.success(nftMarketPlaceService.offerPage(req));
    }

    @PostMapping("activity-page")
    @ApiOperation("活动分页")
    public GenericDto<IPage<NftActivityResp>> activityPage(@Valid @RequestBody NftActivityPageReq req) {
        return GenericDto.success(nftMarketPlaceService.activityPage(req));
    }

    @PostMapping("page-skin")
    @ApiOperation("获取皮肤分页信息")
    public GenericDto<List<NftMarketPlaceSkinResp>> skinQueryPage(@RequestBody NftMarketPlaceSkinPageReq skinQuery){
        return GenericDto.success(nftMarketPlaceService.skinQueryPage(skinQuery));
    }

    @PostMapping("page-equip")
    @ApiOperation("获取装备分页信息")
    public GenericDto<List<NftMarketPlaceEqiupmentResp>>equipQueryPage(@RequestBody NftMarketPlaceEquipPageReq equipQuery){
        return GenericDto.success(nftMarketPlaceService.equipQueryPage(equipQuery));
    }

    @PostMapping("page-props")
    @ApiOperation("获取道具分页信息")
    public GenericDto<List<NftMarketPlacePropsResp>>equipPropsPage(@RequestBody NftMarketPlacePropsPageReq propsQuery){
        return GenericDto.success(nftMarketPlaceService.propsQueryPage(propsQuery));
    }

    @PostMapping("view")
    @ApiOperation("浏览量")
    public GenericDto<NftMarketPlaceDetailViewResp>view(@RequestBody NftMarketPlaceDetailViewReq req){
        return GenericDto.success(nftMarketPlaceService.view(req));
    }
}
