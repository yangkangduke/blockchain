package com.seeds.account.controller;

import com.seeds.account.dto.NftPriceHisDto;
import com.seeds.account.dto.req.NftPriceHisReq;
import com.seeds.account.dto.resp.NftOfferResp;
import com.seeds.account.service.AccountTradeService;
import com.seeds.account.service.INftOfferService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 账户系统提供的公共交易接口
 *
 * @author hang.yu
 */
@RestController
@Slf4j
@Api(tags = "账户交易公共")
@RequestMapping("/public/account-trade")
public class PublicAccountTradeController {

    @Autowired
    private AccountTradeService accountTradeService;

    @Autowired
    private INftOfferService nftOfferService;

    /**
     *  NFT历史价格
     */
    @PostMapping("/nft-price-his")
    @ApiOperation(value = "NFT历史价格", notes = "NFT历史价格")
    public GenericDto<NftPriceHisDto> nftPriceHis(@Valid @RequestBody NftPriceHisReq req) {
        return GenericDto.success(accountTradeService.nftPriceHis(req));
    }

    @GetMapping("/nft-offer-list/{id}")
    @ApiOperation("出价列表")
    public GenericDto<List<NftOfferResp>> offerList(@PathVariable("id") Long id) {
        return GenericDto.success(nftOfferService.offerList(id));
    }

}
