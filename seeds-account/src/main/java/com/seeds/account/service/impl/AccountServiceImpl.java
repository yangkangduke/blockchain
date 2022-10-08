package com.seeds.account.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.anno.ExecutionLock;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.*;
import com.seeds.account.enums.*;
import com.seeds.account.ex.ActionDeniedException;
import com.seeds.account.ex.MissingElementException;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.account.service.*;
import com.seeds.account.util.AddressUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.common.utils.BasicUtils;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.dto.request.VerifyAuthTokenReq;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.feign.UserCenterFeignClient;
import com.seeds.wallet.dto.SignedMessageDto;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.omg.IOP.TransactionService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.seeds.account.AccountConstants.WITHDRAW_DECIMALS;


/**
 * @author milo
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
    IWithdrawWhitelistService withdrawWhitelistService;
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
        Utils.check(rule != null && rule.getStatus() == CommonStatus.ENABLED, ErrorCode.ACCOUNT_WITHDRAW_DISABLED);
        Utils.check(rule.getFeeAmount().compareTo(fee) == 0, ErrorCode.ACCOUNT_INVALID_WITHDRAW_FEE);

        // 是否是内部提币
        boolean internalWithdraw = false;
//        MetamaskBinding metamaskBinding = null;
        boolean isDefiWithdraw = Chain.SUPPORT_DEFI_LIST.contains(chain);
        if (isDefiWithdraw) {
            // 如果是DEFI提币，目标地址必须是自己的metamask地址
//            metamaskBinding = metamaskBindingMapper.getByUserId(userId);
//            Utils.check(metamaskBinding != null && ObjectUtils.isAddressEquals(metamaskBinding.getAddress(), address), ErrorCode.ACCOUNT_INVALID_WITHDRAW_ADDRESS);
        } else {
            // 如果不是DEFI提币，不允许提币给自己
            ChainDepositAddress assignedDepositAddress = chainDepositService.getByAddress(chain, address);
            if (assignedDepositAddress != null && assignedDepositAddress.getUserId() == userId) {
                Utils.check(assignedDepositAddress.getUserId() != userId, ErrorCode.ACCOUNT_INVALID_WITHDRAW_TARGET);
            }
            // 是否是内部提币
            internalWithdraw = assignedDepositAddress != null && assignedDepositAddress.getUserId() > 0;
        }

        // 读取提币白名单
        WithdrawLimitRuleDto limitRule = chainWithdrawService.getWithdrawLimitRule(currency);
        WithdrawWhitelistDto withdrawWhitelistDto = withdrawWhitelistService.get(userId, currency);
        BigDecimal minAmount = limitRule.getMinAmount();
        BigDecimal maxAmount = withdrawWhitelistDto != null ? withdrawWhitelistDto.getMaxAmount() : limitRule.getMaxAmount();
        BigDecimal intradyAmount = withdrawWhitelistDto != null ? withdrawWhitelistDto.getIntradayAmount() : limitRule.getIntradayAmount();
        BigDecimal autoAmount = withdrawWhitelistDto != null ? withdrawWhitelistDto.getAutoAmount() : limitRule.getAutoAmount();

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
        String publicAddress = withdrawRequestDto.getPublicAddress();
        String signature = withdrawRequestDto.getSignature();
        String msg = withdrawRequestDto.getMsg();

        if (!isDefiWithdraw) {
            if (withdrawToken != null) {
                // 验证用户的提币Token是否有效
                VerifyAuthTokenReq verifyRequest = VerifyAuthTokenReq.builder().uid(userId).authToken(withdrawToken).useType(AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_WITHDRAW).build();
                GenericDto<Boolean> verifyResponse = userCenterFeignClient.verifyToken(verifyRequest);
                log.info("withdraw userId={} currency={} verifyRequest={} verifyResponse={}", userId, currency, verifyRequest, verifyResponse);
                Utils.check(verifyResponse != null && verifyResponse.isSuccess() && verifyResponse.getData(), ErrorCode.ACCOUNT_INVALID_WITHDRAW_AUTHENTICATION);
            } else if (publicAddress != null && signature != null) {
                MetamaskVerifyReq verifyRequest = new MetamaskVerifyReq();
                verifyRequest.setPublicAddress(publicAddress);
                verifyRequest.setSignature(signature);
                verifyRequest.setMessage(msg);
                verifyRequest.setUserId(userId);
                GenericDto<Boolean> verifyResponse = userCenterFeignClient.metaMaskVerifySignature(verifyRequest);
                log.info("withdraw userId={} currency={} verifyRequest={} verifyResponse={}", userId, currency, verifyRequest, verifyResponse);
                Utils.check(verifyResponse != null && verifyResponse.isSuccess() && verifyResponse.getData(), ErrorCode.ACCOUNT_INVALID_WITHDRAW_AUTHENTICATION);
            } else {
                Utils.throwError(ErrorCode.ACCOUNT_INVALID_WITHDRAW_AUTHENTICATION);
            }
        }

        boolean result = walletAccountService.freeze(userId, currency, amount);
        Utils.check(result, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);

        // 是否内部提币返还手续费
        boolean internalWithdrawFeeReturn = Objects.equals("true", limitRule.getZeroFeeOnInternal());
        // 判断是否属于不需要审核的提币
        boolean requireReview = amount.compareTo(autoAmount) > 0 && !isDefiWithdraw;
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

        WithdrawResponseDto withdrawResponseDto = new WithdrawResponseDto();
        if (isDefiWithdraw) {
            // id要保证唯一
            long id = transaction.getId();
            Utils.check(id > 0, "id generation issue");
            String defiWithdrawContract = systemWalletAddressService.getOne(chain.getRelayOn(), WalletAddressType.DEFI_DEPOSIT_WITHDRAW_CONTRACT);
            // 截止时间为当前时间后5分钟
            long deadline = System.currentTimeMillis() / 1000 + Long.parseLong(systemConfigService.getValue(AccountSystemConfig.CHAIN_DEFI_WITHDRAW_DEADLINE, "300"));
            SignedMessageDto signedMessageDto = chainService.encodeAndSignDefiWithdraw(chain.getRelayOn(), id, "metamaskBinding.getAddress()", currency, amount, deadline);
            String signedSignature = AddressUtils.leftPad(signedMessageDto.getSignature(), "0x");
            String signedMessage = AddressUtils.leftPad(signedMessageDto.getMessage(), "0x");
            log.info("signMessage userId={} id={} chain={} currency={} amount={} signedSignature={} signedMessage={}", userId, id, chain, currency, amount, signedSignature, signedMessage);

            withdrawResponseDto.setDefiWithdrawContract(defiWithdrawContract);
            withdrawResponseDto.setSignedSignature(signedSignature);
            withdrawResponseDto.setSignedMessage(signedMessage);
            withdrawResponseDto.setDeadline(deadline);

            // 如果是Defi提币，需要存储下签名的消息, 已备用户后面查询
            transactionService.afterCommit(() -> {
                chainDepositWithdrawHisService.createSigHistory(transaction.getId(), userId, chain, currency, amount, signedSignature, signedMessage, deadline);
            });
        }

//        transactionService.afterCommit(() -> {
//            // 记录历史执行中
//            userAccountActionService.createHistory(userId, currency, AccountAction.WITHDRAW, String.valueOf(transaction.getId()), amount, CommonActionStatus.PROCESSING);
//
//            // 通知账户变更
//            accountPublishService.publishAsync(AccountTopics.TOPIC_ACCOUNT_UPDATE,
//                    AccountUpdateEvent.builder().ts(System.currentTimeMillis()).userId(userId).action(AccountAction.WITHDRAW.getCode()).build());
//
//            // 发送通知给运营人员
//            if (requireReview) {
//                notificationService.sendNotificationAsync(NotificationDto.builder()
//                        .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                        .values(ImmutableMap.of(
//                                "ts", System.currentTimeMillis(),
//                                "module", "withdraw",
//                                "content", transaction.getId() + " pending approval"))
//                        .build());
//            }
//        });

        return withdrawResponseDto;
    }

    @Override
    public void checkFundingStatus() {
        boolean settling = fundingRateActionService.isAnySettlingAsset();
        Utils.check(!settling, ErrorCode.ACCOUNT_IN_SETTLEMENT);
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
