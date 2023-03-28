package com.seeds.game.controller;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.response.*;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.service.INftAttributeService;
import com.seeds.game.service.NftMarketPlaceService;
import com.seeds.game.service.INftPublicBackpackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Api(tags = "NFT市场")
@RestController
@RequestMapping("/web/nft-marketplace")
public class NftMarketPlaceController {

    @Autowired
    private NftMarketPlaceService nftMarketPlaceService;

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

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

    @PostMapping("make-offer")
    @ApiOperation("拍卖出价")
    public GenericDto<Object> makeOffer(@Valid @RequestBody NftMakeOfferReq req) {
        nftMarketPlaceService.makeOffer(req);
        return GenericDto.success(null);
    }

    @PostMapping("buy-success")
    @ApiOperation("购买成功")
    public GenericDto<Object> buySuccess(@Valid @RequestBody NftBuySuccessReq req) {
        nftMarketPlaceService.buySuccess(req);
        return GenericDto.success(null);
    }

    @PostMapping("offer-page")
    @ApiOperation("拍卖出价分页")
    public GenericDto<NftOfferResp> offerPage(@Valid @RequestBody NftOfferPageReq req) {
        return GenericDto.success(nftMarketPlaceService.offerPage(req));
    }

    @PostMapping("accept-offer")
    @ApiOperation("接受拍卖出价")
    public GenericDto<Object> acceptOffer(@Valid @RequestBody NftAcceptOfferReq req) {
        nftMarketPlaceService.acceptOffer(req);
        return GenericDto.success(null);
    }

    @PostMapping("cancel-offer")
    @ApiOperation("取消拍卖出价")
    public GenericDto<Object> cancelOffer(@Valid @RequestBody NftCancelOfferReq req) {
        nftMarketPlaceService.cancelOffer(req);
        return GenericDto.success(null);
    }

    @PostMapping("activity-page")
    @ApiOperation("活动分页")
    public GenericDto<NftActivityResp> activityPage(@Valid @RequestBody NftActivityPageReq req) {
        return GenericDto.success(nftMarketPlaceService.activityPage(req));
    }

}
