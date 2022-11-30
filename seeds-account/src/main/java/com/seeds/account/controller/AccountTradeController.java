package com.seeds.account.controller;

import com.seeds.account.dto.req.NftBuyReq;
import com.seeds.account.service.AccountTradeService;
import com.seeds.common.dto.GenericDto;
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

}
