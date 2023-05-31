package com.seeds.game.controller;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.response.NftMyOfferResp;
import com.seeds.game.dto.response.NftOfferDetailResp;
import com.seeds.game.service.INftAttributeService;
import com.seeds.game.service.NftMarketPlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;

@Api(tags = "NFT市场")
@RestController
@RequestMapping("/web/nft-marketplace")
public class NftMarketPlaceController {

    @Autowired
    private NftMarketPlaceService nftMarketPlaceService;

    @Autowired
    private INftAttributeService nftAttributeService;

    @PostMapping("fixed-price-shelf")
    @ApiOperation("固定价格上架")
    public GenericDto<Object> fixedPriceShelf(@Valid @RequestBody NftFixedPriceShelfReq req) {
        nftMarketPlaceService.fixedPriceShelf(req);
        return GenericDto.success(null);
    }

    @PostMapping("british-auction-shelf")
    @ApiOperation("英式拍卖上架")
    public GenericDto<Object> britishAuctionShelf(@Valid @RequestBody NftBritishAuctionShelfReq req) {
        nftMarketPlaceService.britishAuctionShelf(req);
        return GenericDto.success(null);
    }

    @PostMapping("shelved")
    @ApiOperation("下架")
    public GenericDto<Object> shelved(@Valid @RequestBody NftShelvedReq req) {
        nftMarketPlaceService.shelved(req);
        return GenericDto.success(null);
    }

    @PostMapping("cancel-auction")
    @ApiOperation("取消拍卖")
    public GenericDto<Object> cancelAuction(@Valid @RequestBody NftCancelAuctionReq req) {
        nftMarketPlaceService.cancelAuction(req);
        return GenericDto.success(null);
    }

    @PostMapping("make-offer")
    @ApiOperation("拍卖出价")
    public GenericDto<Object> makeOffer(@Valid @RequestBody NftMakeOfferReq req) {
        nftMarketPlaceService.makeOffer(req);
        return GenericDto.success(null);
    }

    @GetMapping("make-offer-validate/{auctionId}")
    @ApiOperation("拍卖出价验证")
    public GenericDto<Boolean> makeOfferValidate(@PathVariable String auctionId) {
        return GenericDto.success(nftMarketPlaceService.makeOfferValidate(auctionId));
    }

    @PostMapping("my-offer-page")
    @ApiOperation("我的拍卖出价分页")
    public GenericDto<IPage<NftMyOfferResp>> myOfferPage(@Valid @RequestBody NftMyOfferPageReq req) {
        return GenericDto.success(nftMarketPlaceService.myOfferPage(req));
    }

    @PostMapping("buy-success")
    @ApiOperation("购买成功")
    public GenericDto<Object> buySuccess(@Valid @RequestBody NftBuySuccessReq req) {
        nftMarketPlaceService.buySuccess(req);
        return GenericDto.success(null);
    }

    @PostMapping("accept-offer")
    @ApiOperation("接受拍卖出价")
    public GenericDto<NftOfferDetailResp> acceptOffer(@Valid @RequestBody NftAcceptOfferReq req) {
        return GenericDto.success(nftMarketPlaceService.acceptOffer(req));
    }

    @PostMapping("auction-success")
    @ApiOperation("拍卖达成交易")
    public GenericDto<Object> auctionSuccess(@Valid @RequestBody NftSaleSuccessReq req) {
        nftMarketPlaceService.auctionSuccess(req);
        return GenericDto.success(null);
    }

    @PostMapping("cancel-offer")
    @ApiOperation("取消拍卖出价")
    public GenericDto<Object> cancelOffer(@Valid @RequestBody NftCancelOfferReq req) {
        nftMarketPlaceService.cancelOffer(req);
        return GenericDto.success(null);
    }

    @GetMapping("custodian-fee")
    @ApiOperation("获取托管费")
    public GenericDto<BigDecimal> custodianFee(@RequestParam BigDecimal price, @RequestParam(required = false) Long duration) {
        return GenericDto.success(nftMarketPlaceService.custodianFee(price, duration));
    }

    @PostMapping("refund-all-fee")
    @ApiOperation("托管费退还")
    public GenericDto<Object> refundAllFee(@Valid @RequestBody NftRefundAllFeeReq req) {
        nftMarketPlaceService.refundAllFee(req);
        return GenericDto.success(null);
    }

    @GetMapping("list-receipt/{orderId}")
    @ApiOperation("获取订单收据")
    public GenericDto<JSONObject> listReceipt(@PathVariable Long orderId) {
        return GenericDto.success(nftMarketPlaceService.listReceipt(orderId));
    }

}
