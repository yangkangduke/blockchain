package com.seeds.uc.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
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
     *  出价接口
     */
    @PostMapping("/bids")
    @ApiOperation(value = "出价", notes = "出价")
    @Inner
    public GenericDto<Object> bids(@Valid @RequestBody NFTMakeOfferReq req) {
        ucInterNFTService.bids(req);
        return GenericDto.success(null);
    }

}
