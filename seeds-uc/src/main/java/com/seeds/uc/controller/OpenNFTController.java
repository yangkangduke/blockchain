package com.seeds.uc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysNftPageReq;
import com.seeds.admin.dto.request.UcSwitchReq;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
import com.seeds.uc.service.IUcNftOfferService;
import com.seeds.uc.service.IUcUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/nft")
@Api(tags = "NFT交易")
@Slf4j
public class OpenNFTController {

    @Autowired
    private IUcUserAccountService ucUserAccountService;
    @Autowired
    private IUcNftOfferService ucNftOffersService;
    @Autowired
    private RemoteNftService remoteNftService;

    @PostMapping("/buy")
    @ApiOperation(value = "购买", notes = "购买")
    public GenericDto<Object> buyNFT(@Valid @RequestBody NFTBuyReq buyReq) {
        ucUserAccountService.buyNFT(buyReq);
        return GenericDto.success(null);
    }

    @PostMapping("/owner-page")
    @ApiOperation(value = "用户拥有分页查询", notes = "用户拥有分页查询")
    public GenericDto<Page<SysNftResp>> ownerPage(@Valid @RequestBody SysNftPageReq query) {
        query.setUserId(UserContext.getCurrentUserId());
        return remoteNftService.ucPage(query);
    }

    @PostMapping("/uc-switch")
    @ApiOperation("uc上架/下架")
    public GenericDto<Object> ucUpOrDown(@Valid @RequestBody UcSwitchReq req) {
        req.setUcUserId(UserContext.getCurrentUserId());
        return remoteNftService.ucUpOrDown(req);
    }

    @PostMapping("/make-offer")
    @ApiOperation("NFT出价")
    public GenericDto<Object> makeOffer(@Valid @RequestBody NFTMakeOfferReq req) {
        ucNftOffersService.makeOffer(req);
        return GenericDto.success(null);
    }

    @PostMapping("/offer-reject/{id}")
    @ApiOperation("NFT竞价拒绝")
    public GenericDto<Object> offerReject(@PathVariable("id") Long id) {
        ucNftOffersService.offerReject(id);
        return GenericDto.success(null);
    }

    @PostMapping("/offer-accept/{id}")
    @ApiOperation("NFT竞价接受")
    public GenericDto<Object> offerAccept(@PathVariable("id") Long id) {
        ucNftOffersService.offerAccept(id);
        return GenericDto.success(null);
    }

}
