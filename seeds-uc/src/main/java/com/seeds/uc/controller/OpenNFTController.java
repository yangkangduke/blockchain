package com.seeds.uc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.enums.AccountActionEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.model.UcUserAccount;
import com.seeds.uc.service.IUcUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

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
public class OpenNFTController {

    @Autowired
    private IUcUserAccountService ucUserAccountService;


    @PostMapping("/buy")
    @ApiOperation(value = "购买", notes = "购买")
    public GenericDto<Object> buyNFT(@Valid @RequestBody NFTBuyReq buyReq) {
        BigDecimal amount = buyReq.getAmount();
        Long currentUserId = UserContext.getCurrentUserId();

        if (!ucUserAccountService.checkBalance(currentUserId, amount)) {
            throw new GenericException(UcErrorCodeEnum.ERR_18004_ACCOUNT_BALANCE_INSUFFICIENT);
        }
        // todo 远程调用钱包接口
        // todo 远程调用admin端改变NFT的归属人接口，uc端只记录交易的流水状态在进行中，等admin端执行成功后在调用uc端的回调接口
        // todo 增加交易记录


        return GenericDto.success(null);
    }

    /**
     * todo 改变NFT的归属人失败的回调接口
     */
    @PostMapping("/buy/callback")
    @ApiOperation(value = "购买回调接口", notes = "购买回调接口")
    @Inner
    public GenericDto<Object> buyNFTCallback(@Valid @RequestBody NFTBuyReq buyReq) {

        // 对应的账户中金额变更并且增加交易记录
        ucUserAccountService.action(AccountActionReq.builder()
                .action(AccountActionEnum.BUY_NFT)
                .amount(buyReq.getAmount())
                .comments(buyReq.getComments())
                .fromAddress("")
                .toAddress("")
                .fee(buyReq.getFee())
                .fromUserId(buyReq.getFromUserId())
            .build());

        // todo 改变交易记录的状态

        return GenericDto.success(null);
    }

}
