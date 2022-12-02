package com.seeds.account.controller;

import com.seeds.account.dto.NftGasFeesDto;
import com.seeds.account.dto.req.*;
import com.seeds.account.dto.resp.NftOfferResp;
import com.seeds.account.service.AccountTradeService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.account.dto.resp.NftAuctionResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 账户系统提供的内部调用交易接口
 *
 * @author hang.yu
 */
@RestController
@Slf4j
@Api(tags = "账户交易-内部调用")
@RequestMapping("/account-trade-internal")
public class AccountTradeInternalController {

    @Autowired
    private AccountTradeService accountTradeService;

    /**
     *  购买NFT回调接口
     */
    @PostMapping("/buy-nft-callback")
    @ApiOperation(value = "购买NFT回调接口", notes = "购买NFT回调接口")
    @Inner
    public GenericDto<Object> buyNftCallback(@Valid @RequestBody NftBuyCallbackReq buyReq) {
        accountTradeService.buyNftCallback(buyReq);
        return GenericDto.success(null);
    }

    /**
     *  购买NFT
     */
    @PostMapping("/buy-nft")
    @ApiOperation(value = "购买NFT", notes = "购买NFT")
    @Inner
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
    @Inner
    public GenericDto<Object> reverseAuction(@Valid @RequestBody NftReverseAuctionReq req) {
        accountTradeService.reverseAuction(req);
        return GenericDto.success(null);
    }

    /**
     *  正向出价
     */
    @PostMapping("/nft-forward-bids")
    @ApiOperation(value = "正向出价", notes = "正向出价")
    @Inner
    public GenericDto<Object> forwardBids(@Valid @RequestBody NftMakeOfferReq req) {
        accountTradeService.forwardBids(req);
        return GenericDto.success(null);
    }

    /**
     *  反向出价
     */
    @PostMapping("/nft-reverse-bids")
    @ApiOperation(value = "反向出价", notes = "反向出价")
    @Inner
    public GenericDto<Object> reverseBids(@Valid @RequestBody NftBuyReq req) {
        accountTradeService.reverseBids(req);
        return GenericDto.success(null);
    }

    /**
     *  出价列表
     */
    @GetMapping("/nft-offer-list")
    @ApiOperation("NFT出价列表")
    @Inner
    public GenericDto<List<NftOfferResp>> offerList(@RequestParam Long id) {
        return GenericDto.success(accountTradeService.offerList(id));
    }

    /**
     *  NFT拍卖信息
     */
    @GetMapping("/nft-action-info")
    @ApiOperation("NFT拍卖信息")
    @Inner
    public GenericDto<NftAuctionResp> actionInfo(@RequestParam Long id, @RequestParam Long userId) {
        return GenericDto.success(accountTradeService.actionInfo(id, userId));
    }

    /**
     *  手续费扣除接口
     */
    @PostMapping("/nft-deduct-gas-fee")
    @ApiOperation(value = "手续费扣除", notes = "手续费扣除")
    @Inner
    public GenericDto<Object> nftDeductGasFee(@Valid @RequestBody AccountOperateReq req) {
        accountTradeService.nftDeductGasFee(req);
        return GenericDto.success(null);
    }

    /**
     *  NFT手续费
     */
    @GetMapping("/nft-gas-fees")
    @ApiOperation(value = "NFT手续费", notes = "NFT手续费")
    @Inner
    public GenericDto<List<NftGasFeesDto>> nftGasFee() {
        return GenericDto.success(accountTradeService.nftGasFee());
    }

    /**
     *  解冻金额
     */
    @PostMapping("/amount-unfreeze")
    @ApiOperation(value = "解冻金额", notes = "解冻金额")
    @Inner
    public GenericDto<Object> amountUnfreeze(@Valid @RequestBody AccountOperateReq req) {
        accountTradeService.amountUnfreeze(req);
        return GenericDto.success(null);
    }

    /**
     *  余额变更
     */
    @PostMapping("/amount-change-balance")
    @ApiOperation(value = "余额变更", notes = "余额变更")
    @Inner
    public GenericDto<Object> amountChangeBalance(@Valid @RequestBody AccountOperateReq req) {
        accountTradeService.amountChangeBalance(req);
        return GenericDto.success(null);
    }

    /**
     *  NFT的offer过期任务
     */
    @PostMapping("/job/nft-offer-expired")
    @ApiOperation(value = "NFT的offer过期任务", notes = "NFT的offer过期任务")
    @Inner
    public GenericDto<Object> nftOfferExpired() {
        accountTradeService.nftOfferExpired();
        return GenericDto.success(null);
    }

}
