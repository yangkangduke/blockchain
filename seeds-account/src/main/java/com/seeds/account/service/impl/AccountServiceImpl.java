package com.seeds.account.service.impl;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.seeds.account.AccountConstants;
import com.seeds.account.anno.ExecutionLock;
import com.seeds.account.calc.AccountCalculator;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.*;
import com.seeds.account.enums.*;
import com.seeds.account.ex.ActionDeniedException;
import com.seeds.account.ex.MissingElementException;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.account.model.UserAccount;
import com.seeds.account.sender.KafkaProducer;
import com.seeds.account.service.*;
import com.seeds.account.util.AddressUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.account.util.Utils;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.common.enums.TargetSource;
import com.seeds.common.utils.BasicUtils;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.enums.NoticeTypeEnum;
import com.seeds.uc.dto.request.VerifyAuthTokenReq;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.seeds.account.AccountConstants.WITHDRAW_DECIMALS;


/**
 * @author yk
 */
@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    IChainService chainService;
    @Autowired
    IActionControlService actionControlService;
    @Autowired
    IBlacklistAddressService blacklistAddressService;
    @Autowired
    IChainWithdrawService chainWithdrawService;
    @Autowired
    IChainDepositService chainDepositService;
    @Autowired
    IWithdrawRuleUserService withdrawRuleUserService;
    @Autowired
    IChainDepositWithdrawHisService chainDepositWithdrawHisService;
    @Autowired
    UserCenterFeignClient userCenterFeignClient;
    @Autowired
    IWalletAccountService walletAccountService;
    @Autowired
    ISystemWalletAddressService systemWalletAddressService;
    @Autowired
    ISystemConfigService systemConfigService;
    @Autowired
    IFundingRateActionService fundingRateActionService;
    @Autowired
    ITransactionService transactionService;
    @Autowired
    IUserAccountActionService userAccountActionService;
//    @Autowired
//    IPriceService priceService;

    @Autowired
    private KafkaProducer kafkaProducer;


    @Override
    @ExecutionLock(key = "account:exec:lock:{userId}")
    @Transactional(rollbackFor = Exception.class)
    public WithdrawResponseDto withdraw(long userId, WithdrawRequestDto withdrawRequestDto) throws Exception {
        log.info("withdraw userId={} withdrawRequestDto={}", userId, withdrawRequestDto);

        checkFundingStatus();

        boolean enabled = actionControlService.isEnabled(AccountActionControl.CHAIN_WITHDRAW);
        Utils.check(enabled, ErrorCode.ACCOUNT_WITHDRAW_DISABLED);

        Chain chain = Chain.fromCode(withdrawRequestDto.getChain());
        Utils.check(chain != null, ErrorCode.ACCOUNT_INVALID_CHAIN);

        String currency = withdrawRequestDto.getCurrency();
        BigDecimal amount = withdrawRequestDto.getAmount();
        BigDecimal fee = withdrawRequestDto.getFee();
        String address = withdrawRequestDto.getAddress();

        // 检查提币黑名单
        boolean isInBlacklist = blacklistAddressService.get(ChainAction.WITHDRAW.getCode(), userId, address) != null;
        Utils.check(!isInBlacklist, ErrorCode.ACCOUNT_INVALID_WITHDRAW_BLACK_ADDRESS);

        Utils.check(currency != null && currency.length() > 0, ErrorCode.ACCOUNT_INVALID_WITHDRAW_CURRENCY);
        Utils.check(amount != null && amount.signum() > 0 && (amount.scale() <= WITHDRAW_DECIMALS || amount.stripTrailingZeros().scale() <= WITHDRAW_DECIMALS), ErrorCode.ACCOUNT_INVALID_WITHDRAW_AMOUNT);
        Utils.check(fee != null && fee.signum() >= 0, ErrorCode.ACCOUNT_INVALID_WITHDRAW_FEE);
        Utils.check(address != null && address.length() > 0 && AddressUtils.validate(chain, address), ErrorCode.ACCOUNT_INVALID_WITHDRAW_ADDRESS);

        // 检查提币规则是否启用
        WithdrawRuleDto rule = chainWithdrawService.getWithdrawRule(chain, currency);
        Utils.check(rule != null && rule.getStatus() == CommonStatus.ENABLED.getCode(), ErrorCode.ACCOUNT_WITHDRAW_DISABLED);
        Utils.check(rule.getFeeAmount().compareTo(fee) == 0, ErrorCode.ACCOUNT_INVALID_WITHDRAW_FEE);

        // 是否是内部提币
        boolean internalWithdraw = false;

        // 自己不能提币给自己
        ChainDepositAddress assignedDepositAddress = chainDepositService.getByAddress(chain, address);
        if (assignedDepositAddress != null && assignedDepositAddress.getUserId() == userId) {
            Utils.check(assignedDepositAddress.getUserId() != userId, ErrorCode.ACCOUNT_INVALID_WITHDRAW_TARGET);
        }
        // 是否是内部提币
        internalWithdraw = assignedDepositAddress != null && assignedDepositAddress.getUserId() > 0;

        // 读取提币用户规则
        WithdrawRuleUserDto withdrawRuleUserDto = withdrawRuleUserService.get(userId, currency);
        BigDecimal minAmount = rule.getMinAmount();
        BigDecimal maxAmount = withdrawRuleUserDto != null ? withdrawRuleUserDto.getMaxAmount() : rule.getMaxAmount();
        BigDecimal intradyAmount = withdrawRuleUserDto != null ? withdrawRuleUserDto.getIntradayAmount() : rule.getIntradayAmount();
        BigDecimal autoAmount = withdrawRuleUserDto != null ? withdrawRuleUserDto.getAutoAmount() : rule.getAutoAmount();

        // 校验提币精度
        int decimals = rule.getDecimals();
        Utils.check(amount.scale() <= decimals || amount.stripTrailingZeros().scale() <= decimals, ErrorCode.ACCOUNT_INVALID_WITHDRAW_AMOUNT);
        // 校验提币最小值
        Utils.check(amount.compareTo(minAmount) >= 0, ErrorCode.ACCOUNT_INVALID_WITHDRAW_AMOUNT_LESS_THAN_MIN);
        // 校验提币最大值
        Utils.check(amount.compareTo(maxAmount) <= 0, ErrorCode.ACCOUNT_INVALID_WITHDRAW_AMOUNT_GREATER_THAN_MAX);
        // 校验提币手续费
        BigDecimal feeAmount = rule.getFeeAmount();
        Utils.check(amount.compareTo(feeAmount) > 0, ErrorCode.ACCOUNT_INVALID_WITHDRAW_FEE);
        // 校验日限额
        BigDecimal usedIntradayWithdraw = getUsedIntradayWithdraw(userId, currency);
        Utils.check(amount.add(usedIntradayWithdraw).compareTo(intradyAmount) <= 0, ErrorCode.ACCOUNT_INVALID_WITHDRAW_EXCEED_INTRADAY_AMOUNT);

        log.info("withdraw userId={} currency={} amount={} feeAmount={} minAmount={} maxAmount={} intradyAmount={} autoAmount={} usedIntradayWithdraw={}",
                userId, currency, amount, feeAmount, minAmount, maxAmount, intradyAmount, autoAmount, usedIntradayWithdraw);

        String withdrawToken = withdrawRequestDto.getWithdrawToken();

        if (withdrawToken != null) {
            // 验证用户的提币Token是否有效
            VerifyAuthTokenReq verifyRequest = VerifyAuthTokenReq.builder()
                    .uid(userId)
                    .authToken(withdrawToken)
                    .useType(AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_WITHDRAW)
                    .build();
            GenericDto<Boolean> verifyResponse = userCenterFeignClient.verifyToken(verifyRequest);
            log.info("withdraw userId={} currency={} verifyRequest={} verifyResponse={}", userId, currency, verifyRequest, verifyResponse);
            Utils.check(verifyResponse != null && verifyResponse.isSuccess() && verifyResponse.getData(), ErrorCode.ACCOUNT_INVALID_WITHDRAW_AUTHENTICATION);
        } else {
            Utils.throwError(ErrorCode.ACCOUNT_INVALID_WITHDRAW_AUTHENTICATION);
        }

        boolean result = walletAccountService.freeze(userId, currency, amount);
        Utils.check(result, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);

        // 是否内部提币返还手续费
        boolean internalWithdrawFeeReturn = Objects.equals("true", rule.getZeroFeeOnInternal());
        // 判断是否属于不需要审核的提币
        boolean requireReview = amount.compareTo(autoAmount) > 0;
        int manual = requireReview ? 1 : 0;
        int status = requireReview ? WithdrawStatus.PENDING_APPROVE.getCode() : WithdrawStatus.CREATED.getCode();
        ChainDepositWithdrawHis transaction = ChainDepositWithdrawHis.builder()
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .version(AccountConstants.DEFAULT_VERSION)
                .userId(userId)
                .chain(chain)
                .action(ChainAction.WITHDRAW)
                .fromAddress("")
                .toAddress(address)
                .txToAddress("")
                .currency(currency)
                .internal(internalWithdraw ? 1 : 0)
                .amount(amount)
                .feeAmount((internalWithdraw && internalWithdrawFeeReturn) ? BigDecimal.ZERO : fee)
                .txFee(BigDecimal.ZERO)
                .gasPrice(0L)
                .gasLimit(0L)
                .blockNumber(0L)
                .blockHash("")
                .txHash("")
                .txValue("0")
                .nonce("0")
                .isReplace(false)
                .manual(manual)
                .blacklist(0)
                .comments("")
                .status(status)
                .build();
        // 插入新的充提
        chainDepositWithdrawHisService.createHistory(transaction);
        // 提币审核通知
        if (WithdrawStatus.PENDING_APPROVE.getCode() == status) {
            kafkaProducer.send(KafkaTopic.TOPIC_ACCOUNT_AUDIT, JSONUtil.toJsonStr(NotificationReq.builder()
                    .notificationType(NoticeTypeEnum.ACCOUNT_AUDIT.getCode())
                    .userSource(TargetSource.ADMIN.name())
                    .values(ImmutableMap.of(
                            "ts", System.currentTimeMillis(),
                            "type", AccountAction.WITHDRAW.getNotificationType()))
                    .build()));
            log.info("send audit notification, ts:{}, type:{}", System.currentTimeMillis(), AccountAction.WITHDRAW.getNotificationType());
        }

        WithdrawResponseDto withdrawResponseDto = new WithdrawResponseDto();

        transactionService.afterCommit(() -> {
            // 记录历史执行中
            userAccountActionService.createHistory(userId, currency, AccountAction.WITHDRAW, String.valueOf(transaction.getId()), amount, CommonActionStatus.PROCESSING);

        });

        return withdrawResponseDto;
    }

    @Override
    public void checkFundingStatus() {
        boolean settling = fundingRateActionService.isAnySettlingAsset();
        Utils.check(!settling, ErrorCode.ACCOUNT_IN_SETTLEMENT);
    }

    @Override
    public UserAccountSummaryDto getUserWalletAccountSummaryDto(long userId) {
//        Map<String, BigDecimal> priceMap = priceService.getLatestPriceMap();
        Map<String, BigDecimal> priceMap = new HashMap<>();
        priceMap.put("USDT", new BigDecimal(1));
        return getUserWalletAccountSummaryDto(userId, priceMap);
    }

    @Override
    public UserAccountSummaryDto getUserWalletAccountSummaryDto(long userId, Map<String, BigDecimal> priceMap) {
        List<UserAccount> accounts = walletAccountService.getAccounts(userId);
        UserAccountSummaryDto userAccountSummaryDto = AccountCalculator.calculateAccountSummary(
                accounts.stream().map(e -> ObjectUtils.copy(e, new UserAccountDto())).collect(Collectors.toList()),
                priceMap);

        Set<String> existingCurrencies = userAccountSummaryDto.getAccounts().stream().map(UserAccountDto::getCurrency).collect(Collectors.toSet());
        chainDepositService.getAllDepositRules().forEach(rule -> {
            String currency = rule.getCurrency();
            if (!existingCurrencies.contains(currency)) {
                userAccountSummaryDto.getAccounts().add(AccountCalculator.createWalletAccountOnFly(currency, priceMap.get(currency)));
                existingCurrencies.add(currency);
            }
        });
        return userAccountSummaryDto;
    }

    @Override
    @ExecutionLock(key = "account:deposit-withdraw:lock:{id}")
    @Transactional(rollbackFor = Exception.class)
    public void rejectTransaction(long id, String comments) {
        ChainDepositWithdrawHis tx = chainDepositWithdrawHisService.getHistory(id);
        log.info("rejectTransaction id={} comments={} tx={}", id, comments, tx);
        if (tx == null) {
            throw new MissingElementException("transaction not exists");
        }

        if (tx.getAction() == ChainAction.DEPOSIT) {
            if (!Objects.equals(tx.getStatus(), DepositStatus.PENDING_APPROVE.getCode())) {
                throw new ActionDeniedException("status is not pending approval");
            }
            int manual = 1;
            int status = DepositStatus.REJECTED.getCode();
            tx.setUpdateTime(System.currentTimeMillis());
            tx.setVersion(tx.getVersion() + 1);
            tx.setManual(manual);
            tx.setStatus(status);
            tx.setComments(comments != null ? comments : "");
            chainDepositWithdrawHisService.updateHistory(tx);
            // todo 应该需要把用户提币成功的钱返还给用户（表示提币整个过程失败）
        } else if (tx.getAction() == ChainAction.WITHDRAW) {
            if (!Objects.equals(tx.getStatus(), WithdrawStatus.PENDING_APPROVE.getCode())) {
                throw new ActionDeniedException("status is not pending approve");
            }
            int manual = 1;
            int status = WithdrawStatus.REJECTED.getCode();
            tx.setUpdateTime(System.currentTimeMillis());
            tx.setVersion(tx.getVersion() + 1);
            tx.setManual(manual);
            tx.setStatus(status);
            tx.setComments(comments != null ? comments : "");
            chainDepositWithdrawHisService.updateHistory(tx);

            // 解冻资产
            boolean done = walletAccountService.unfreeze(tx.getUserId(), tx.getCurrency(), tx.getAmount());
            Utils.check(done, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);
            // 更新财务记录状态为失败
            userAccountActionService.updateStatusByActionUserIdSource(AccountAction.WITHDRAW, tx.getUserId(), String.valueOf(tx.getId()), CommonActionStatus.FAILED);

            transactionService.afterCommit(() -> {

                // 发送通知用户提币被拒绝
                kafkaProducer.sendAsync(KafkaTopic.TOPIC_ACCOUNT_UPDATE,JSONUtil.toJsonStr(NotificationReq.builder()
                        .notificationType(NoticeTypeEnum.ACCOUNT_WITHDRAW_REJECTED.getCode())
                        .userSource(TargetSource.UC.name())
                        .ucUserIds(ImmutableList.of(tx.getUserId()))
                        .values(ImmutableMap.of(
                                "ts", System.currentTimeMillis(),
                                "currency", tx.getCurrency(),
                                "amount", tx.getAmount()))
                        .build()));
                log.info("send withdraw_rejected notification ts:{},currency:{},amount:{}", System.currentTimeMillis(), tx.getCurrency(), tx.getAmount());

            });
        }
    }

    @Override
    @ExecutionLock(key = "account:deposit-withdraw:lock:{id}")
    @Transactional(rollbackFor = Exception.class)
    public void approveTransaction(long id, String comments) {
        ChainDepositWithdrawHis transaction = chainDepositWithdrawHisService.getHistory(id);
        log.info("approveTransaction id={} comments={} transaction={}", id, comments, transaction);
        if (transaction == null) {
            throw new MissingElementException("transaction not exists");
        }

        if (transaction.getAction() == ChainAction.DEPOSIT) {
            if (!Objects.equals(transaction.getStatus(), DepositStatus.PENDING_APPROVE.getCode())) {
                throw new ActionDeniedException("status is not pending approve");
            }
            int manual = 1;
            int status = DepositStatus.TRANSACTION_CONFIRMED.getCode();
            transaction.setUpdateTime(System.currentTimeMillis());
            transaction.setVersion(transaction.getVersion() + 1);
            transaction.setManual(manual);
            // 不用标记成APPROVED，直接标记成上账
            transaction.setStatus(status);
            transaction.setComments(comments != null ? comments : "");
            chainDepositWithdrawHisService.updateHistory(transaction);

            // 用户资产上账
            boolean result = walletAccountService.updateAvailable(transaction.getUserId(), transaction.getCurrency(), transaction.getAmount(), true);
            Utils.check(result, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);

            transactionService.afterCommit(() -> {
                // 记录用户财务历史
                userAccountActionService.createHistory(transaction.getUserId(), transaction.getCurrency(), AccountAction.DEPOSIT, transaction.getAmount());

                List<UserAccount> accounts = walletAccountService.getAccounts(transaction.getUserId());
                // 通知账户变更
                kafkaProducer.send(KafkaTopic.TOPIC_ACCOUNT_UPDATE, JSONUtil.toJsonStr(NotificationReq.builder()
                        .notificationType(NoticeTypeEnum.ACCOUNT_BALANCE_CHANGE.getCode())
                        .userSource(TargetSource.UC.name())
                        .ucUserIds(ImmutableList.of(transaction.getUserId()))
                        .values(ImmutableMap.of(
                                "ts", System.currentTimeMillis(),
                                "currency", transaction.getCurrency(),
                                "change", transaction.getAmount(),
                                "after", CollectionUtils.isEmpty(accounts) ? "" : accounts.get(0).getAvailable()))
                        .build()));
                log.info("send balance change notification userid:{}, ts:{},currency:{},change:{}", transaction.getUserId(), System.currentTimeMillis(), transaction.getCurrency(), transaction.getAmount());


                // 发送通知给客户
                kafkaProducer.send(KafkaTopic.TOPIC_ACCOUNT_UPDATE, JSONUtil.toJsonStr(NotificationReq.builder()
                        .notificationType(NoticeTypeEnum.ACCOUNT_DEPOSIT.getCode())
                        .userSource(TargetSource.UC.name())
                        .ucUserIds(ImmutableList.of(transaction.getUserId()))
                        .values(ImmutableMap.of(
                                "ts", System.currentTimeMillis(),
                                "currency", transaction.getCurrency(),
                                "amount", transaction.getAmount()))
                        .build()));
                log.info("send deposit notification userid:{}, ts:{},currency:{},amount:{}", transaction.getUserId(), System.currentTimeMillis(), transaction.getCurrency(), transaction.getAmount());
            });
        } else if (transaction.getAction() == ChainAction.WITHDRAW) {
            if (!Objects.equals(transaction.getStatus(), WithdrawStatus.PENDING_APPROVE.getCode())) {
                throw new ActionDeniedException("status is not pending approval");
            }
            int manual = 1;
            int status = WithdrawStatus.APPROVED.getCode();
            transaction.setUpdateTime(System.currentTimeMillis());
            transaction.setVersion(transaction.getVersion() + 1);
            transaction.setManual(manual);
            transaction.setStatus(status);
            transaction.setComments(comments != null ? comments : "");
            chainDepositWithdrawHisService.updateHistory(transaction);
        }
    }

    @Override
    public BigDecimal getUsedIntradayWithdraw(long userId, String currency) {
        long startTime = BasicUtils.toDays();
        List<ChainDepositWithdrawHis> list = chainDepositWithdrawHisService.getIntradayWithdraw(userId, currency, startTime);
        BigDecimal usedIntradayWithdraw = list.stream()
                .filter(e -> !Objects.equals(e.getStatus(), WithdrawStatus.TRANSACTION_CANCELLED.getCode()))
                .map(ChainDepositWithdrawHis::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("getIntradayWithdraw userId={} currency={} startTime={} list={} usedIntradayWithdraw={}", userId, currency, startTime, list, usedIntradayWithdraw);
        return usedIntradayWithdraw;
    }
}
