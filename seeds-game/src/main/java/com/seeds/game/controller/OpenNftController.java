package com.seeds.game.controller;

import com.seeds.account.dto.NftGasFeesDto;
import com.seeds.account.dto.req.AccountOperateReq;
import com.seeds.account.feign.RemoteAccountTradeService;
import com.seeds.admin.enums.SysOwnerTypeEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.TargetSource;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.*;
import com.seeds.game.service.UcUserService;
import com.seeds.uc.exceptions.GenericException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 提供外部调用的NFT相关接口
 * @author hang.yu
 * @date 2022/10/08
 */
@Slf4j
@Api(tags = "NFT外部调用授权")
@RestController
@RequestMapping("/nft")
public class OpenNftController {

    @Autowired
    private RemoteAccountTradeService remoteAccountTradeService;

    @Autowired
    private RemoteNftService adminRemoteNftService;

    @Autowired
    private UcUserService ucUserService;

    @PostMapping("create")
    @ApiOperation("NFT创建")
    public GenericDto<Long> create(@RequestBody OpenNftCreateReq req) {
        // 手续费
        AccountOperateReq operateReq = nftDeductGasFee(new BigDecimal(req.getGasFees()), req.getUnit());
        req.setOwnerId(UserContext.getCurrentUserId());
        req.setOwnerType(SysOwnerTypeEnum.UC_USER.getCode());
        GenericDto<Long> result = adminRemoteNftService.create(req);
        if (!result.isSuccess()) {
            remoteAccountTradeService.amountUnfreeze(operateReq);
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("upgrade")
    @ApiOperation("NFT升级")
    public GenericDto<Long> upgrade(@RequestBody OpenNftUpgradeReq req) {
        // 手续费
        AccountOperateReq operateReq = nftDeductGasFee(new BigDecimal(req.getGasFees()), req.getUnit());
        req.setOwnerId(UserContext.getCurrentUserId());
        req.setOwnerType(SysOwnerTypeEnum.UC_USER.getCode());
        GenericDto<Long> result = adminRemoteNftService.upgrade(req);
        if (!result.isSuccess()) {
            remoteAccountTradeService.amountUnfreeze(operateReq);
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("lock")
    @ApiOperation("NFT锁定")
    public GenericDto<Object> lock(@Valid @RequestBody OpenNftLockReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<Object> result = adminRemoteNftService.lock(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("buy")
    @ApiOperation("NFT购买")
    public GenericDto<Object> buy(@Valid @RequestBody OpenNftBuyReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        req.setSource(TargetSource.GAME);
        GenericDto<Object> result = remoteAccountTradeService.buyNft(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("forward-auction")
    @ApiOperation("NFT正向拍卖")
    public GenericDto<Object> forwardAuction(@Valid @RequestBody OpenNftForwardAuctionReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<Object> result = remoteAccountTradeService.forwardAuction(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("reverse-auction")
    @ApiOperation("NFT反向拍卖")
    public GenericDto<Object> reverseAuction(@Valid @RequestBody OpenNftReverseAuctionReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<Object> result = remoteAccountTradeService.reverseAuction(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("forward-bids")
    @ApiOperation("NFT正向出价")
    public GenericDto<Object> forwardBids(@Valid @RequestBody OpenNftForwardBidsReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<Object> result = remoteAccountTradeService.forwardBids(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("reverse-bids")
    @ApiOperation("NFT反向出价")
    public GenericDto<Object> reverseBids(@Valid @RequestBody OpenNftReverseBidsReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<Object> result = remoteAccountTradeService.reverseBids(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("shelves")
    @ApiOperation("NFT上架")
    public GenericDto<Object> shelves(@Valid @RequestBody OpenNftShelvesReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<Object> result = adminRemoteNftService.shelves(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("sold-out")
    @ApiOperation("NFT下架")
    public GenericDto<Object> soldOut(@Valid @RequestBody OpenNftSoldOutReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<Object> result = adminRemoteNftService.soldOut(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @GetMapping("/gas-fees")
    @ApiOperation("NFT费用")
    public GenericDto<List<NftGasFeesDto>> gasFees(@RequestParam String accessKey,
                                                   @RequestParam String signature,
                                                   @RequestParam Long timestamp) {
        GenericDto<List<NftGasFeesDto>> result = remoteAccountTradeService.nftGasFee();
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @GetMapping("user-trade-times")
    @ApiOperation("用户交易次数")
    public GenericDto<Integer> userTradeTimes(@RequestParam String accessKey,
                                              @RequestParam String signature,
                                              @RequestParam Long timestamp) {
        return GenericDto.success(ucUserService.userTradeTimes(UserContext.getCurrentUserId()));
    }

    private AccountOperateReq nftDeductGasFee(BigDecimal amount, String currency) {
        AccountOperateReq operateReq = AccountOperateReq.builder()
                .amount(amount)
                .userId(UserContext.getCurrentUserId())
                .currency(currency)
                .build();
        GenericDto<Object> gasFeeResult = remoteAccountTradeService.nftDeductGasFee(operateReq);
        if (!gasFeeResult.isSuccess()) {
            throw new GenericException(gasFeeResult.getMessage());
        }
        return operateReq;
    }

}
