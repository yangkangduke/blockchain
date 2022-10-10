package com.seeds.uc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysNftPageReq;
import com.seeds.admin.dto.request.UcSwitchReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.enums.NftStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.service.IUcNftOfferService;
import com.seeds.uc.service.IUcUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

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
        Long currentUserId = UserContext.getCurrentUserId();
        BigDecimal price;
        SysNftDetailResp sysNftDetailResp;
        try {
            GenericDto<SysNftDetailResp> sysNftDetailRespGenericDto = remoteNftService.ucDetail(buyReq.getNftId());
            sysNftDetailResp = sysNftDetailRespGenericDto.getData();
            price = sysNftDetailResp.getPrice();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18005_ACCOUNT_BUY_FAIL);
        }
        //  判断nft是否是上架状态、nft是否已经购买过了
        if (!Objects.isNull(sysNftDetailResp)) {
            if (sysNftDetailResp.getStatus() != NftStatusEnum.ON_SALE.getCode()) {
                throw new GenericException(UcErrorCodeEnum.ERR_18006_ACCOUNT_BUY_FAIL_INVALID_NFT_STATUS);
            }
            // 判断NFT是否已锁定
            if (WhetherEnum.YES.value() == sysNftDetailResp.getLockFlag()) {
                throw new GenericException(UcErrorCodeEnum.ERR_18007_ACCOUNT_BUY_FAIL_NFT_LOCKED);
            }
        }


        // 检查账户里面的金额是否足够支付
        if (!ucUserAccountService.checkBalance(currentUserId, price)) {
            throw new GenericException(UcErrorCodeEnum.ERR_18004_ACCOUNT_BALANCE_INSUFFICIENT);
        }
        ucUserAccountService.buyNFTFreeze(sysNftDetailResp, buyReq.getSource());

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
