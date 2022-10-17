package com.seeds.account.controller;

import com.seeds.account.AccountConstants;
import com.seeds.account.dto.UserAccountSummaryDto;
import com.seeds.account.dto.WithdrawRequestDto;
import com.seeds.account.dto.WithdrawResponseDto;
import com.seeds.account.service.IAccountService;
import com.seeds.account.service.IChainDepositService;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * @program: seeds-java
 * @description: 账户
 * @author: yk
 * @create: 2022-09-28 10:17
 **/
@RestController
@Slf4j
@Api(tags = "账户")
@RequestMapping("/account")
public class AccountController {

    @Autowired
    IChainDepositService chainDepositService;
    @Autowired
    IAccountService accountService;


    /**
     * 获取钱包账户汇总
     *
     * @return
     */
    @GetMapping("/wallet-accounts")
    @ApiOperation("获取钱包账户汇总")
    public GenericDto<UserAccountSummaryDto> getWalletAccounts() {
        try {
            long userId = getUserId();
            UserAccountSummaryDto summaryDto = accountService.getUserWalletAccountSummaryDto(userId);
            return GenericDto.success(summaryDto);
        } catch (Exception e) {
            log.error("getWalletAccounts", e);
            return Utils.returnFromException(e);
        }
    }


    /**
     * 获取充币地址
     *
     * @param chain default to Chain.ETH.getCode()
     * @return
     */
    @GetMapping("/deposit-address")
    @ApiOperation("获取用户的充币地址")
    public GenericDto<String> getDepositAddress(@RequestParam(value = "chain", defaultValue = "1") int chain) {
        try {
            long userId = getUserId();
            return GenericDto.success(chainDepositService.getDepositAddress(Chain.fromCode(chain), userId, true));
        } catch (Exception e) {
            log.error("getDepositAddress", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 从网关获取当前用户的Id做初步校验
     *
     * @return
     */
    private long getUserId() {
        long currentUserId = UserContext.getCurrentUserId();
        log.debug("currentUserId={}", currentUserId);
        Assert.isTrue(currentUserId >= AccountConstants.getClientUserId(), "invalid user");
        return currentUserId;
    }


    /**
     * 提币
     *
     * @param withdrawRequestDto
     * @return
     */
    @PostMapping("/withdraw")
    @ApiOperation("提交提币请求")
    public GenericDto<WithdrawResponseDto> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto) {
        try {
            long userId = getUserId();
            WithdrawResponseDto withdrawResponseDto = accountService.withdraw(userId, withdrawRequestDto);
            return GenericDto.success(withdrawResponseDto);
        } catch (Exception e) {
            log.error("withdraw", e);
            return Utils.returnFromException(e);
        }
    }
}
