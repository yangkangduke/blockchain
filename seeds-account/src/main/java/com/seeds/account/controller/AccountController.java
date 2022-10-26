package com.seeds.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.*;
import com.seeds.account.dto.req.AccountHistoryReq;
import com.seeds.account.enums.CommonStatus;
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

import java.math.BigDecimal;
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
@RequestMapping("/account-web")
public class AccountController {

    @Autowired
    private IChainDepositService chainDepositService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IChainDepositWithdrawHisService chainDepositWithdrawHisService;
    @Autowired
    private IChainWithdrawService chainWithdrawService;
    @Autowired
    private IWithdrawWhitelistService withdrawWhitelistService;
    @Autowired
    private IChainContractService chainContractService;


    @GetMapping("/wallet-accounts")
    @ApiOperation(value = "获取钱包账户汇总", notes = "获取钱包账户汇总")
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
    @ApiOperation(value = "获取用户的充币地址", notes = "chain的传值，1：eth链erc20  3：tron链trc20")
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
    @ApiOperation(value = "提交提币请求", notes = "1.调用发送邮件/email/send接口，传参数VERIFY_SETTING_POLICY_WITHDRAW 2.调用strategy/verify接口返回authToken 3.调用该接口")
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

    /**
     * 获取单币种的充币信息
     *
     * @param currency
     * @return
     */
    @GetMapping("/deposit-rules")
    @ApiOperation("获取单币种的充币限制信息")
    public GenericDto<List<Map<String, Object>>> getDepositRules(@RequestParam("currency") String currency) {
        try {
            List<DepositRuleDto> rules = chainDepositService.getAllDepositRules()
                    .stream()
                    .filter(e -> Objects.equals(e.getCurrency(), currency) && e.getStatus() == CommonStatus.ENABLED)
                    .collect(Collectors.toList());
            List<Map<String, Object>> list = Lists.newArrayList();
            rules.forEach(e -> {
                Map<String, Object> values = Maps.newLinkedHashMap();
                values.put("chain", e.getChain());

                Chain chain = Chain.fromCode(e.getChain());
                ChainContractDto chainContractDto = chainContractService.get(Chain.SUPPORT_DEFI_LIST.contains(chain) ? chain.getRelayOn().getCode() : chain.getCode(), e.getCurrency());
                values.put("contractAddress", chainContractDto != null ? chainContractDto.getAddress() : "");
                values.put("contractDecimal", chainContractDto != null ? chainContractDto.getDecimals() : "0");
                list.add(values);
            });
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getDepositRules", e);
            return Utils.returnFromException(e);
        }
    }

    @GetMapping("/withdraw-rules")
    @ApiOperation("获取单币种的提币限制信息")
    public GenericDto<List<Map<String, Object>>> getWithdrawRules(@RequestParam("currency") String currency) {
        try {
            long userId = getUserId();

            List<WithdrawRuleDto> rules = chainWithdrawService.getWithdrawRules()
                    .stream()
                    .filter(e -> Objects.equals(e.getCurrency(), currency) && e.getStatus() == CommonStatus.ENABLED)
                    .collect(Collectors.toList());
            List<Map<String, Object>> list = Lists.newArrayList();
            rules.forEach(e -> {
                Map<String, Object> values = Maps.newLinkedHashMap();
                values.put("chain", e.getChain().getCode());
                values.put("feeType", e.getFeeType());
                values.put("feeAmount", e.getFeeAmount());
                values.put("amountDecimals", e.getDecimals());

                WithdrawLimitRuleDto limitRuleDto = chainWithdrawService.getWithdrawLimitRule(e.getCurrency());
                BigDecimal minAmount = limitRuleDto.getMinAmount();
                BigDecimal maxAmount = limitRuleDto.getMaxAmount();
                BigDecimal intradayAmount = limitRuleDto.getIntradayAmount();
                BigDecimal autoAmount = limitRuleDto.getAutoAmount();

                WithdrawWhitelistDto withdrawWhitelistDto = withdrawWhitelistService.get(userId, e.getCurrency());
                if (withdrawWhitelistDto != null) {
                    maxAmount = withdrawWhitelistDto.getMaxAmount();
                    intradayAmount = withdrawWhitelistDto.getIntradayAmount();
                    autoAmount = withdrawWhitelistDto.getAutoAmount();
                }

                values.put("minAmount", minAmount);
                values.put("maxAmount", maxAmount);
                values.put("intradayAmount", intradayAmount);
                values.put("autoAmount", autoAmount);
                values.put("zeroFeeOnInternal", limitRuleDto.getZeroFeeOnInternal());

                // 当日已使用额度
                BigDecimal usedIntradayWithdraw = accountService.getUsedIntradayWithdraw(userId, e.getCurrency());
                values.put("usedIntradayWithdraw", usedIntradayWithdraw);

                // 合约信息
                Chain chain = Chain.fromCode(e.getChain().getCode());
                ChainContractDto chainContractDto = chainContractService.get(Chain.SUPPORT_DEFI_LIST.contains(chain) ? chain.getRelayOn().getCode() : chain.getCode(), e.getCurrency());
                values.put("contractAddress", chainContractDto != null ? chainContractDto.getAddress() : "");
                values.put("contractDecimal", chainContractDto != null ? chainContractDto.getDecimals() : "0");

                list.add(values);
            });
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getWithdrawRules", e);
            return Utils.returnFromException(e);
        }
    }

    @GetMapping("/user-rules")
    @ApiOperation("获取充币规则、提币规则(暂时没有用)")
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
