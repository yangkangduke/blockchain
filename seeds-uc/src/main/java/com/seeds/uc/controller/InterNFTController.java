package com.seeds.uc.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.service.UcInterNFTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/internal-nft")
@Api(tags = "NFT交易内部调用")
public class InterNFTController {

    @Autowired
    private UcInterNFTService ucInterNFTService;

    /**
     *  购买回调接口
     */
    @PostMapping("/buy/callback")
    @ApiOperation(value = "购买回调", notes = "购买回调")
    @Inner
    public GenericDto<Object> buyNFTCallback(@Valid @RequestBody NFTBuyCallbackReq buyReq) {
        ucInterNFTService.buyNFTCallback(buyReq);
        return GenericDto.success(null);
    }

    /**
     *  购买接口
     */
    @PostMapping("/buy")
    @ApiOperation(value = "购买", notes = "购买")
    @Inner
    public GenericDto<Object> buyNFT(@Valid @RequestBody NFTBuyReq buyReq) {
        ucInterNFTService.buyNFT(buyReq);
        return GenericDto.success(null);
    }

    /**
     *  正向拍卖
     */
    @PostMapping("/forward-auction")
    @ApiOperation(value = "正向拍卖", notes = "正向拍卖")
    @Inner
    public GenericDto<Object> forwardAuction(@Valid @RequestBody NFTForwardAuctionReq req) {
        ucInterNFTService.forwardAuction(req);
        return GenericDto.success(null);
    }

    /**
     *  反向拍卖
     */
    @PostMapping("/reverse-auction")
    @ApiOperation(value = "反向拍卖", notes = "反向拍卖")
    @Inner
    public GenericDto<Object> reverseAuction(@Valid @RequestBody NFTReverseAuctionReq req) {
        ucInterNFTService.reverseAuction(req);
        return GenericDto.success(null);
    }

    /**
     *  正向出价
     */
    @PostMapping("/forward-bids")
    @ApiOperation(value = "正向出价", notes = "正向出价")
    @Inner
    public GenericDto<Object> forwardBids(@Valid @RequestBody NFTMakeOfferReq req) {
        ucInterNFTService.forwardBids(req);
        return GenericDto.success(null);
    }

    /**
     *  反向出价
     */
    @PostMapping("/reverse-bids")
    @ApiOperation(value = "反向出价", notes = "反向出价")
    @Inner
    public GenericDto<Object> reverseBids(@Valid @RequestBody NFTBuyReq req) {
        ucInterNFTService.reverseBids(req);
        return GenericDto.success(null);
    }

}
