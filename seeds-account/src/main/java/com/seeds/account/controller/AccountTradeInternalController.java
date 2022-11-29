package com.seeds.account.controller;

import com.seeds.account.dto.req.NftBuyCallbackReq;
import com.seeds.account.service.AccountTradeService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
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

}
