package com.seeds.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.*;
import com.seeds.account.dto.req.AccountHistoryReq;
import com.seeds.account.service.*;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.web.context.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private IChainDepositService chainDepositService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IChainDepositWithdrawHisService chainDepositWithdrawHisService;
    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    private IChainWithdrawService chainWithdrawService;


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


    @PostMapping("/deposit-withdraw-history")
    @ApiOperation("获取充提历史")
    public GenericDto<IPage<ChainDepositWithdrawHisDto>> getDepositWithdrawHistory(@RequestBody AccountHistoryReq accountHistoryReq) {
        try {
            long userId = getUserId();
            accountHistoryReq.setUserId(userId);
            Page page = new Page();
            page.setCurrent(accountHistoryReq.getCurrent());
            page.setSize(accountHistoryReq.getSize());
            IPage<ChainDepositWithdrawHisDto> list = chainDepositWithdrawHisService.getHistory(page, accountHistoryReq);
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getDepositWithdrawHistory", e);
            return Utils.returnFromException(e);
        }
    }

    @GetMapping("/user-rules")
    @ApiOperation("获取充币规则、提币规则")
    public GenericDto<Map<String, Object>> getAllRules() {
        Map<String, Object> rules = Maps.newLinkedHashMap();
        rules.put("depositRules", getCurrentUserDepositRules());
        rules.put("withdrawRules", getCurrentUserWithdrawRules());
        rules.put("withdrawLimitRules", chainWithdrawService.getWithdrawLimitRules());
        return GenericDto.success(rules);
    }

    private List<DepositRuleDto> getCurrentUserDepositRules() {
        return chainDepositService.getAllDepositRules();
    }

    private List<WithdrawRuleDto> getCurrentUserWithdrawRules() {
        return chainWithdrawService.getWithdrawRules();
    }
}
