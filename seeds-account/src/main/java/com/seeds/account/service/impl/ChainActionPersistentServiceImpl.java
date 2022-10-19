package com.seeds.account.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.seeds.account.AccountConstants;
import com.seeds.account.chain.dto.NativeChainBlockDto;
import com.seeds.account.chain.dto.NativeChainTransactionDto;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.ChainAction;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.enums.DepositStatus;
import com.seeds.account.ex.ActionDeniedException;
import com.seeds.account.mapper.ChainBlockMapper;
import com.seeds.account.mapper.ChainDepositAddressMapper;
import com.seeds.account.mapper.ChainDepositWithdrawHisMapper;
import com.seeds.account.model.ChainBlock;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.account.sender.KafkaProducer;
import com.seeds.account.service.*;
import com.seeds.account.util.Utils;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.notification.dto.request.NotificationReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * 关注事务操作
 *
 * @author yk
 *
 */
@Slf4j
@Service
public class ChainActionPersistentServiceImpl implements IChainActionPersistentService {

    @Autowired
    IUserAccountActionService userAccountActionService;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private ChainBlockMapper chainBlockMapper;
    @Autowired
    private ChainDepositWithdrawHisMapper chainDepositWithdrawHisMapper;
    @Autowired
    private IChainDepositService chainDepositService;
    @Autowired
    private ChainDepositAddressMapper chainDepositAddressMapper;
    @Autowired
    private IWalletAccountService walletAccountService;
//    @Autowired
//    AccountAutoExchangeService accountAutoExchangeService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public void insert(List<ChainDepositAddress> list) {
        list.forEach(e -> {
            log.info("insert new address={}", e);
            chainDepositAddressMapper.insert(e);
        });
    }

    @Override
    public void rollbackBlock(Chain chain, long blockNumber, String blockHash) {
        int rollbackBlocks = chainBlockMapper.rollbackBlock(chain, blockNumber, blockHash, CommonStatus.DISABLED.getCode());
        int rollbackTransactions = chainDepositWithdrawHisMapper.rollbackByBlockNumberAndBlockHash(chain, blockNumber, blockHash, DepositStatus.CANCELLED.getCode());
        log.info("rollbackBlock chain={} blockHash={} blockNumber={} rollbackBlocks={} rollbackTransactions={}", chain, blockHash, blockNumber, rollbackBlocks, rollbackTransactions);
    }

    /**
     *
     * @param newBlockDto 需要入库的
     * @param assignedList 已经绑定用户的地址
     * @param transactions 需要入库的
     * @param toSuspendedList 需要更新状态为suspended
     * @param toConfirmList 需要更新状态为上账并上账
     */
    @Override
    public void processNewBlock(NativeChainBlockDto newBlockDto,
                                List<ChainDepositAddress> assignedList, List<NativeChainTransactionDto> transactions,
                                List<ChainDepositWithdrawHis> toSuspendedList, List<ChainDepositWithdrawHis> toConfirmList) {
        if (newBlockDto == null) {
            return;
        }
        ChainBlock chainBlock = ChainBlock.builder()
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .version(AccountConstants.DEFAULT_VERSION)
                .chain(newBlockDto.getChain())
                .blockNumber(newBlockDto.getBlockNumber())
                .blockTime(newBlockDto.getBlockTime())
                .blockHash(newBlockDto.getBlockHash())
                .parentHash(newBlockDto.getParentHash())
                .status(CommonStatus.ENABLED)
                .build();
        log.info("insert new chainBlock={}", chainBlock);
        // 增加新的block
        chainBlockMapper.insert(chainBlock);

        // 挂起黑地址充值
        if (toSuspendedList.size() > 0) {
            toSuspendedList.forEach(transaction -> {
                int manual = 1;
                int status = DepositStatus.PENDING_APPROVE.getCode();
                log.info("suspend transaction={}", transaction);
                transaction.setUpdateTime(System.currentTimeMillis());
                transaction.setVersion(transaction.getVersion() + 1);
                // 设置为等待人工审核
                transaction.setManual(manual);
                transaction.setStatus(status);
                // 标记为黑地址
                transaction.setBlacklist(1);
                // 更新
                chainDepositWithdrawHisMapper.updateById(transaction);
                // 发送通知给运维人员
//                notificationService.sendNotificationAsync(NotificationDto.builder()
//                        .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                        .values(ImmutableMap.of(
//                                "ts", System.currentTimeMillis(),
//                                "module", "deposit",
//                                "content", transaction.getId() +" pending approval"))
//                        .build());
            });
        }
        // 确认上账
        if (toConfirmList.size() > 0) {
            toConfirmList.forEach(transaction -> {
                String currency = transaction.getCurrency();
                DepositRuleDto rule = chainDepositService.getDepositRule(transaction.getChain(), currency);
                if (rule != null && rule.getStatus() == CommonStatus.ENABLED) {
                    if (transaction.getAmount().compareTo(rule.getAutoAmount()) <= 0) {
                        // 满足自动上币条件
                        confirmTransaction(transaction);
                    } else {
                        // 超过自动上币额度
                        log.info("set to pending_approve transaction={}", transaction);
                        int manual = 1;
                        int status = DepositStatus.PENDING_APPROVE.getCode();
                        transaction.setUpdateTime(System.currentTimeMillis());
                        transaction.setVersion(transaction.getVersion() + 1);
                        // 设置为等待人工审核
                        transaction.setManual(manual);
                        transaction.setStatus(status);
                        // 更新
                        chainDepositWithdrawHisMapper.updateById(transaction);
                        // 发送通知给运维人员
//                        notificationService.sendNotificationAsync(NotificationDto.builder()
//                                .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                .values(ImmutableMap.of(
//                                        "ts", System.currentTimeMillis(),
//                                        "module", "deposit",
//                                        "content", transaction.getId() +" pending approval"))
//                                .build());
                    }
                }
            });
        }

        if (transactions.size() > 0) {
            // 地址都转成小写用于匹配
            Map<String, Long> addressToUserMap = assignedList.stream().collect(Collectors.toMap(o -> o.getAddress().toLowerCase(), ChainDepositAddress::getUserId));
            for (NativeChainTransactionDto transaction : transactions) {
                Chain chain = transaction.getChain();

                // 从充币地址列表找关联用户
                Long userId = null;

                // 从Metamask登录地址中找关联用户
                if (Chain.SUPPORT_DEFI_LIST.contains(chain)) {
//                    MetamaskBinding metamaskBinding = metamaskBindingMapper.getByAddress(transaction.getFromAddress());
//                    log.info("metamaskBinding fromAddress={} metamaskBinding={}", transaction.getFromAddress(), metamaskBinding);
//                    if (metamaskBinding != null) {
//                        userId = metamaskBinding.getUserId();
//                    }
                } else {
                    userId = addressToUserMap.get(transaction.getToAddress().toLowerCase());
                }

                if (userId == null) {
                    log.warn("not able to find userId for transaction={}", transaction);
                    continue;
                }

                int manual = 0;
                int status = DepositStatus.CREATED.getCode();
                ChainDepositWithdrawHis o = ChainDepositWithdrawHis.builder()
                        .createTime(System.currentTimeMillis())
                        .updateTime(System.currentTimeMillis())
                        .version(AccountConstants.DEFAULT_VERSION)
                        .userId(userId)
                        .chain(chain)
                        .action(ChainAction.DEPOSIT)
                        .fromAddress(transaction.getFromAddress())
                        .toAddress(transaction.getToAddress())
                        // tx toAddress 是业务toAddress, 无法拿到 tx toAddress
                        // 所以此处留空
                        .txToAddress("")
                        .currency(transaction.getCurrency())
                        .internal(0)
                        .amount(transaction.getAmount())
                        .feeAmount(BigDecimal.ZERO)
                        .txFee(transaction.getTxFee())
                        .gasPrice(0L)
                        .gasLimit(0L)
                        .blockNumber(transaction.getBlockNumber())
                        .blockHash(transaction.getBlockHash())
                        .txHash(transaction.getTxHash())
                        .txValue("0")
                        .nonce("0")
                        .isReplace(false)
                        .status(status)
                        .manual(manual)
                        .blacklist(0)
                        .comments("")
                        .build();
                // 插入新的充提
                log.info("insert new deposit transaction={}", o);
                chainDepositWithdrawHisMapper.insert(o);
            }
        }
    }

    @Override
    public void confirmTransaction(ChainDepositWithdrawHis transaction) {
        log.info("confirm transaction={}", transaction);

        Assert.isTrue(transaction != null
                        && transaction.getAction() == ChainAction.DEPOSIT
                        && Objects.equals(transaction.getStatus(), DepositStatus.CREATED.getCode()),
                "invalid transaction" + transaction);

        // 更新充币状态为确认
        int count = chainDepositWithdrawHisMapper.updateStatusByPrimaryKey(transaction.getId(), DepositStatus.TRANSACTION_CONFIRMED.getCode());
        if (count == 0) {
            throw new ActionDeniedException("status no change for transaction id=" + transaction.getId());
        }
        // 用户资产上账
        boolean result = walletAccountService.updateAvailable(transaction.getUserId(), transaction.getCurrency(), transaction.getAmount(), true);
        Utils.check(result, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);

        transactionService.afterCommit(() -> {
            // 记录用户财务历史
            userAccountActionService.createHistory(transaction.getUserId(), transaction.getCurrency(), AccountAction.DEPOSIT, transaction.getAmount());

            // 通知账户变更
//            accountPublishService.publishAsync(AccountTopics.TOPIC_ACCOUNT_UPDATE,
//                    AccountUpdateEvent.builder().ts(System.currentTimeMillis()).userId(transaction.getUserId()).action(AccountAction.DEPOSIT.getCode()).build());

            // 充币自动兑换 （不需要审核的外部充币）
//            accountAutoExchangeService.exchange(transaction.getUserId(), transaction.getCurrency(), transaction.getAmount(), transaction.getInternal() == 1);


            // 发送通知给客户
            kafkaProducer.sendAsync(KafkaTopic.TOPIC_ACCOUNT_UPDATE, NotificationReq.builder()
                    .notificationType(AccountAction.DEPOSIT.getNotificationType())
                    .ucUserIds(ImmutableList.of(transaction.getUserId()))
                    .values(ImmutableMap.of(
                            "ts", System.currentTimeMillis(),
                            "currency", transaction.getCurrency(),
                            "amount", transaction.getAmount()))
                    .build());
        });
    }
}
