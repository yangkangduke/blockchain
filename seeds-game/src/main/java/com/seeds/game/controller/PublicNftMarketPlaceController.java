package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.NftMarketPlaceDetailViewReq;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlacePropsPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.*;
import com.seeds.game.dto.request.*;
import com.seeds.game.service.NftMarketPlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import javax.validation.Valid;

/**
 * NFT市场外部调用
 *
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

    @GetMapping("detail/{nftId}")
    @ApiOperation("详细信息")
    public GenericDto<NftMarketPlaceDetailResp> detail(@PathVariable("nftId") Long nftId) {
        return GenericDto.success(nftMarketPlaceService.detail(nftId));
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
    public GenericDto<IPage<NftMarketPlaceSkinResp>> skinQueryPage(@Valid @RequestBody NftMarketPlaceSkinPageReq skinQuery) {
        return GenericDto.success(nftMarketPlaceService.skinQueryPage(skinQuery));
    }

    @PostMapping("page-equip")
    @ApiOperation("获取装备分页信息")
    public GenericDto<IPage<NftMarketPlaceEqiupmentResp>> equipQueryPage(@Valid @RequestBody NftMarketPlaceEquipPageReq equipQuery) {
        return GenericDto.success(nftMarketPlaceService.equipQueryPage(equipQuery));
    }

    @PostMapping("page-props")
    @ApiOperation("获取道具分页信息")
    public GenericDto<IPage<NftMarketPlacePropsResp>> equipPropsPage(@Valid @RequestBody NftMarketPlacePropsPageReq propsQuery) {
        return GenericDto.success(nftMarketPlaceService.propsQueryPage(propsQuery));
    }

    @PostMapping("view")
    @ApiOperation("浏览量")
    public GenericDto<Object> view(@RequestBody NftMarketPlaceDetailViewReq req) {
        nftMarketPlaceService.view(req);
        return GenericDto.success(null);
    }

    @GetMapping("usd-rate/{currency}")
    @ApiOperation("获取美元汇率")
    public GenericDto<BigDecimal> usdRate(@PathVariable String currency) {
        return GenericDto.success(nftMarketPlaceService.usdRate(currency));
    }

    @GetMapping("chain-nonce")
    @ApiOperation("获取随机码")
    public GenericDto<String> chainNonce() {
        return GenericDto.success(nftMarketPlaceService.chainNonce());
    }

    @PostMapping("refund-fee")
    @ApiOperation("托管费退还")
    public GenericDto<Object> refundFee(@Valid @RequestBody NftRefundFeeReq req) {
        nftMarketPlaceService.refundFee(req);
        return GenericDto.success(null);
    }

    @GetMapping("getTransaction")
    @ApiOperation("getTransaction")
    public GenericDto<TransactionMessageRespDto> getTransaction(@RequestParam("transaction") String transaction) {
        return GenericDto.success(nftMarketPlaceService.getTransaction(transaction));
    }

}
