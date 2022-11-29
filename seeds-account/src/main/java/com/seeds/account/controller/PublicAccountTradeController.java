package com.seeds.account.controller;

import com.seeds.account.dto.NftPriceHisDto;
import com.seeds.account.dto.req.NftPriceHisReq;
import com.seeds.account.service.AccountTradeService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 账户系统提供的公共交易接口
 *
 * @author hang.yu
 */
@RestController
@Slf4j
@Api(tags = "账户交易")
@RequestMapping("/public/account-trade")
public class PublicAccountTradeController {

    @Autowired
    private AccountTradeService accountTradeService;

    /**
     *  NFT历史价格
     */
    @PostMapping("/nft-price-his")
    @ApiOperation(value = "NFT历史价格", notes = "NFT历史价格")
    public GenericDto<NftPriceHisDto> nftPriceHis(@Valid @RequestBody NftPriceHisReq req) {
        return GenericDto.success(accountTradeService.nftPriceHis(req));
    }

}
