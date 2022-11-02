package com.seeds.uc.controller;


import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.NFTAuctionResp;
import com.seeds.uc.dto.response.NFTOfferResp;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.service.UcInterNFTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @Autowired
    private RemoteNftService adminRemoteNftService;

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
        SysNftDetailResp sysNftDetailResp;
        try {
            sysNftDetailResp = adminRemoteNftService.ucDetail(buyReq.getNftId()).getData();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18005_ACCOUNT_BUY_FAIL);
        }
        ucInterNFTService.buyNFT(buyReq, sysNftDetailResp);
        return GenericDto.success(null);
    }

    /**
     *  手续费扣除接口
     */
    @PostMapping("/deduct-gas-fee")
    @ApiOperation(value = "手续费扣除", notes = "手续费扣除")
    @Inner
    public GenericDto<Object> deductGasFee(@Valid @RequestBody NFTDeductGasFeeReq req) {
        ucInterNFTService.deductGasFee(req);
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

    /**
     *  出价列表
     */
    @GetMapping("/offer-list")
    @ApiOperation("NFT出价列表")
    @Inner
    public GenericDto<List<NFTOfferResp>> offerList(@RequestParam Long id) {
        return GenericDto.success(ucInterNFTService.offerList(id));
    }

    /**
     *  NFT拍卖信息
     */
    @GetMapping("/action-info")
    @ApiOperation("NFT拍卖信息")
    @Inner
    public GenericDto<NFTAuctionResp> actionInfo(@RequestParam Long id, @RequestParam Long userId) {
        return GenericDto.success(ucInterNFTService.actionInfo(id, userId));
    }

    /**
     *  上架
     */
    @PostMapping("/shelves")
    @ApiOperation(value = "上架", notes = "上架")
    @Inner
    public GenericDto<Object> shelves(@Valid @RequestBody NFTShelvesReq req) {
        ucInterNFTService.shelves(req);
        return GenericDto.success(null);
    }

    /**
     *  下架
     */
    @PostMapping("/sold-out")
    @ApiOperation(value = "下架", notes = "下架")
    @Inner
    public GenericDto<Object> soldOut(@Valid @RequestBody NFTSoldOutReq req) {
        ucInterNFTService.soldOut(req);
        return GenericDto.success(null);
    }


}
