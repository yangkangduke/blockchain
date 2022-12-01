package com.seeds.account.controller;

import com.seeds.account.dto.req.NftBuyReq;
import com.seeds.account.dto.req.NftForwardAuctionReq;
import com.seeds.account.dto.req.NftMakeOfferReq;
import com.seeds.account.dto.req.NftReverseAuctionReq;
import com.seeds.account.service.AccountTradeService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 账户系统提供的交易接口
 *
 * @author hang.yu
 */
@RestController
@Slf4j
@Api(tags = "账户交易")
@RequestMapping("/account-trade")
public class AccountTradeController {

    @Autowired
    private AccountTradeService accountTradeService;

    /**
     *  购买NFT
     */
    @PostMapping("/buy-nft")
    @ApiOperation(value = "购买NFT", notes = "购买NFT")
    public GenericDto<Object> buyNft(@Valid @RequestBody NftBuyReq buyReq) {
        accountTradeService.validateAndInitBuyNft(buyReq);
        return GenericDto.success(null);
    }

    /**
     *  正向拍卖
     */
    @PostMapping("/nft-forward-auction")
    @ApiOperation(value = "正向拍卖", notes = "正向拍卖")
    @Inner
    public GenericDto<Object> forwardAuction(@Valid @RequestBody NftForwardAuctionReq req) {
        accountTradeService.forwardAuction(req);
        return GenericDto.success(null);
    }

    /**
     *  反向拍卖
     */
    @PostMapping("/nft-reverse-auction")
    @ApiOperation(value = "反向拍卖", notes = "反向拍卖")
    public GenericDto<Object> reverseAuction(@Valid @RequestBody NftReverseAuctionReq req) {
        accountTradeService.reverseAuction(req);
        return GenericDto.success(null);
    }

    /**
     *  正向出价
     */
    @PostMapping("/nft-forward-bids")
    @ApiOperation(value = "正向出价", notes = "正向出价")
    public GenericDto<Object> forwardBids(@Valid @RequestBody NftMakeOfferReq req) {
        accountTradeService.forwardBids(req);
        return GenericDto.success(null);
    }

    /**
     *  反向出价
     */
    @PostMapping("/nft-reverse-bids")
    @ApiOperation(value = "反向出价", notes = "反向出价")
    public GenericDto<Object> reverseBids(@Valid @RequestBody NftBuyReq req) {
        accountTradeService.reverseBids(req);
        return GenericDto.success(null);
    }

    @PostMapping("/nft-make-offer")
    @ApiOperation("NFT出价")
    public GenericDto<Object> nftMakeOffer(@Valid @RequestBody NftMakeOfferReq req) {
        accountTradeService.nftMakeOffer(req, null);
        return GenericDto.success(null);
    }

    @PostMapping("/nft-offer-reject/{id}")
    @ApiOperation("NFT竞价拒绝")
    public GenericDto<Object> nftOfferReject(@PathVariable("id") Long id) {
        accountTradeService.nftOfferReject(id);
        return GenericDto.success(null);
    }

    @PostMapping("/nft-offer-accept/{id}")
    @ApiOperation("NFT竞价接受")
    public GenericDto<Object> nftOfferAccept(@PathVariable("id") Long id) {
        accountTradeService.nftOfferAccept(id);
        return GenericDto.success(null);
    }

}
