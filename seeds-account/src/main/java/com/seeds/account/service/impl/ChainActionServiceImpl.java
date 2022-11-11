package com.seeds.account.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.anno.SingletonLock;
import com.seeds.account.chain.dto.ChainTransaction;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import com.seeds.account.chain.dto.NativeChainBlockDto;
import com.seeds.account.chain.dto.NativeChainTransactionDto;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.*;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.enums.*;
import com.seeds.account.ex.AccountException;
import com.seeds.account.mapper.*;
import com.seeds.account.model.*;
import com.seeds.account.sender.KafkaProducer;
import com.seeds.account.service.*;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.account.util.Utils;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.common.enums.TargetSource;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.enums.NoticeTypeEnum;
import com.seeds.wallet.dto.RawTransactionDto;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.seeds.account.AccountConstants.DEPOSIT_DECIMALS;

/**
 * @program: seeds-java
 * @description: chain action
 * @author: yk
 * @create: 2022-09-28 17:36
 **/
@Slf4j
@Service
public class ChainActionServiceImpl implements IChainActionService {

    @Autowired
    private IUserAccountActionService userAccountActionService;
    @Autowired
    private IActionControlService actionControlService;
    @Autowired
    private ChainDepositAddressMapper chainDepositAddressMapper;
    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    private IChainService chainService;
    @Autowired
    private IChainActionPersistentService chainActionPersistentService;
    @Autowired
    private ChainBlockMapper chainBlockMapper;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private AddressCollectHisMapper addressCollectHisMapper;
    @Autowired
    private IWalletAccountService walletAccountService;
    @Autowired
    private IChainDepositService chainDepositService;
    @Autowired
    private ChainDepositWithdrawHisMapper chainDepositWithdrawHisMapper;
    @Autowired
    private ISystemWalletAddressService systemWalletAddressService;
    @Autowired
    private IBlacklistAddressService blacklistAddressService;
    @Autowired
    private ChainTxnDataMapper chainTxnDataMapper;
    @Autowired
    private ChainTxnReplaceMapper chainTxnReplaceMapper;
    @Autowired
    private ChainExchangeHisMapper chainExchangeHisMapper;
    @Autowired
    private ChainDividendHisMapper chainDividendHisMapper;
    @Autowired
    private IChainDepositWithdrawHisService chainDepositWithdrawHisService;
    @Autowired
    private IChainContractService chainContractService;

    @Autowired
    private KafkaProducer kafkaProducer;

    private final static Event UNISWAP_SWAPEVENT = new Event("Swap",
            Arrays.asList(
                    new TypeReference<Address>(true) {
                    }, new TypeReference<Uint256>() {
                    }, new TypeReference<Uint256>() {
                    }, new TypeReference<Uint256>() {
                    }, new TypeReference<Uint256>() {
                    }, new TypeReference<Address>(true) {
                    }));


    @Override
    @SingletonLock(key = "/seeds/account/chain/scan-idle-addresses")
    public void scanAndCreateAddresses() throws Exception {
        boolean enabled = actionControlService.isEnabled(AccountActionControl.CREATE_CHAIN_ADDRESS);
        if (!enabled) {
            return;
        }
        // 2021-04-27 Milo 扫描需要创建地址的链 (遇到BSC链就映射成ETH链)
        for (Chain chain : Chain.SUPPORT_CREATE_ADDRESS_LIST) {
            chain = Chain.mapChain(chain);
            int chainCode = chain.getCode();

            int idles = chainDepositAddressMapper.countIdleAddresses(chainCode);
            int minIdles = Integer.parseInt(systemConfigService.getValue(AccountSystemConfig.CHAIN_MIN_IDLE_ADDRESS, "10"));
            if (idles >= minIdles) {
                continue;
            }
            int slot = minIdles - idles;
            if (slot <= 0) {
                continue;
            }
            try {
                log.info("checkAndCreateAddresses chain={} minIdles={} idles={} slot={}", chain, minIdles, idles, slot);
                List<String> addresses = chainService.createAddresses(chain, slot);
                if (addresses.size() > 0) {
                    List<ChainDepositAddress> list = addresses.stream()
                            .map(address -> ChainDepositAddress.builder()
                                    .createTime(System.currentTimeMillis())
                                    .updateTime(System.currentTimeMillis())
                                    .version(AccountConstants.DEFAULT_VERSION)
                                    .chain(String.valueOf(chainCode))
                                    .address(address)
                                    .userId(AccountConstants.NOBODY_USER_ID)
                                    .status(CommonStatus.ENABLED.getCode())
                                    .build()
                            ).collect(Collectors.toList());
                    // 交给一个事务service
                    chainActionPersistentService.insert(list);
                }
            } catch (Exception e) {
                log.error("checkAndCreateAddresses chain={} minIdles={} idles={} slot={}", chain, minIdles, idles, slot, e);
            }
        }
    }

    @Override
    @SingletonLock(key = "/seeds/account/chain/scan-block")
    public void scanBlock() throws Exception {
        for (Chain chain : Chain.SUPPORT_LIST) {
            try {
                long l1 = System.currentTimeMillis();
                scanBlock(chain);
                long l2 = System.currentTimeMillis();
                log.info("scanBlock chain={} took={}", chain, (l2 - l1));
            } catch (Exception e) {
                log.error("scanBlock chain={}", chain, e);
            }
        }
    }

    @Override
    @SingletonLock(key = "/seeds/account/chain/execute-withdraw")
    public void executeWithdraw() throws Exception {
        boolean enabled = actionControlService.isEnabled(AccountActionControl.CHAIN_WITHDRAW);
        if (!enabled) {
            return;
        }
        // 获取指定状态的提币
        List<ChainDepositWithdrawHis> list = chainDepositWithdrawHisMapper.getListByStatus(
                ChainAction.WITHDRAW.getCode(),
                Lists.newArrayList(WithdrawStatus.CREATED.getCode(), WithdrawStatus.APPROVED.getCode()));
        int watchWithdrawBatch = getWithdrawBatch();
        for (int i = 0; i < list.size() && i < watchWithdrawBatch; i++) {
            ChainDepositWithdrawHis tx = list.get(i);
            Chain chain = tx.getChain();
            // 用事务包住单个的提币，防止由于单个错误导致的回滚
            try {
               if (tx.getInternal() == 1) {
                    // 内部提币
                    transactionService.execute(() -> {
                        executeWithdrawOneInternal(chain, tx);
                        return null;
                    });
                    // 外部提币
                } else {
                    // 2021-03-09 把操作放到事务外边，保证减冻结资产有且唯一执行一次
                    executeWithdrawOne(chain, tx);
                }
            } catch (Exception e) {
                log.error("executeWithdraw chain={} tx={}", chain, tx, e);
            }
        }
    }

    private String findHotWalletAddress(Chain chain, String currency, BigDecimal amount) throws Exception {
        List<String> addresses = Lists.newArrayList(systemWalletAddressService.getList(chain, WalletAddressType.HOT));
        Collections.shuffle(addresses);

        // 需要根据币种，金额，和tx pool来查找热钱包
        for (String address : addresses) {
            // 不要有gas fee
            BigDecimal balance = chainService.getChainTokenBalance(chain, address);
            if (chain == Chain.TRON) {
                if (balance.compareTo(new BigDecimal("5")) < 0) {
                    continue;
                }
            } else {
                if (balance.compareTo(new BigDecimal("0.02")) < 0) {
                    continue;
                }
            }
            if (Objects.equals(currency, chain.getNativeToken())) {
                if (balance.compareTo(amount) >= 0) {
                    log.info("findHotWalletAddress chain={} address={} currency={} balance={} amount={}", chain, address, currency, balance, amount);
                    return address;
                }
            } else {
                balance = chainService.getContractBalance(chain, address, currency);
                if (balance.compareTo(amount) >= 0) {
                    log.info("findHotWalletAddress chain={} address={} currency={} balance={} amount={}", chain, address, currency, balance, amount);
                    return address;
                }
            }
        }
        log.warn("findHotWalletAddress insufficient balance chain={} currency={} amount={}", chain, currency, amount);
        return null;
    }

    private void executeWithdrawOne(Chain chain, ChainDepositWithdrawHis tx) throws Exception {
        log.info("executeWithdrawOne chain={} tx={}", chain, tx);
        String fromAddress = findHotWalletAddress(chain, tx.getCurrency(), tx.getAmount());
        if (fromAddress != null) {
            BigDecimal amountToChain = tx.getAmount().subtract(tx.getFeeAmount());
            long gasPrice = chainService.getGasPrice(chain);
            long gasLimit = chainService.getGasLimit(chain);
            RawTransactionDto txResult = null;
            try {
                txResult = chainService.internalSendTransaction(chain, tx.getCurrency(), fromAddress, tx.getToAddress(), amountToChain, gasPrice, gasLimit);
            } catch (Exception exception) {
                log.error("executeWithdrawOne, failed to send tx, chain={}, fromAddress={}, tx={},", chain, fromAddress, tx, exception);
            }
            log.info("executeWithdrawOne gasPrice={} gasLimit={} txResult={} tx={}", gasPrice, gasLimit, txResult, tx);
            // 用户从冻结账户减去 提币金额
            boolean result1 = walletAccountService.updateFreeze(tx.getUserId(), tx.getCurrency(), tx.getAmount().negate(), true);
            Utils.check(result1, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);
            // 提币手续费账户增加 fee
            if (tx.getFeeAmount().signum() > 0) {
                boolean result2 = walletAccountService.updateAvailable(AccountConstants.getSystemWithdrawFeeUserId(), tx.getCurrency(), tx.getFeeAmount(), true);
                Utils.check(result2, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);
            }
            if (txResult != null && txResult.getTxnHash() != null && txResult.getTxnHash().length() > 0) {
                tx.setUpdateTime(System.currentTimeMillis());
                tx.setVersion(tx.getVersion() + 1);
                tx.setFromAddress(fromAddress);
                tx.setTxToAddress(txResult.getTo());
                tx.setTxHash(txResult.getTxnHash());
                tx.setTxValue(txResult.getValue().toString());
                tx.setNonce(txResult.getNonce().toString());
                tx.setGasPrice(gasPrice);
                tx.setGasLimit(gasLimit);
                tx.setStatus(WithdrawStatus.TRANSACTION_ON_CHAIN.getCode());
                // 更新状态
                log.info("executeWithdrawOne update={}", tx);
                chainDepositWithdrawHisMapper.updateById(tx);
                // add new chain transaction data his
                ChainTxnData data = ChainTxnData.builder()
                        .version(AccountConstants.DEFAULT_VERSION)
                        .appId(tx.getId())
                        .appType(ChainTxnDataAppType.WITHDRAW)
                        .data(txResult.getData())
                        .createTime(System.currentTimeMillis())
                        .updateTime(System.currentTimeMillis())
                        .build();
                chainTxnDataMapper.insert(data);
                log.info("insert new transaction data={}", data);
            } else {
                // 如果链上tx发送失败，标记为 失败-待处理，运营介入，做replay
                processWithdrawTxException(tx, fromAddress);
            }
        }
    }

    private void processWithdrawTxException(ChainDepositWithdrawHis tx, String fromAddress) {
        // 为了防止提币无限重复，标记为 失败-待处理
        tx.setUpdateTime(System.currentTimeMillis());
        tx.setVersion(tx.getVersion() + 1);
        tx.setFromAddress(fromAddress);
        tx.setTxToAddress("");
        tx.setStatus(WithdrawStatus.TRANSACTION_FAILED_PENDING_CHECK.getCode());
        log.info("executeWithdrawOne update={}", tx);
        // 更新状态
        chainDepositWithdrawHisMapper.updateById(tx);

//        transactionService.afterCommit(() -> {
            // 发送运维
//            notificationService.sendNotificationAsync(NotificationDto.builder()
//                    .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                    .values(ImmutableMap.of(
//                            "ts", System.currentTimeMillis(),
//                            "module", "withdraw",
//                            "content", tx.getId() + " failed"))
//                    .build());
//        });
    }

    /**
     * 内部提币不用上链
     *
     * @param tx
     * @throws Exception
     */
    private void executeWithdrawOneInternal(Chain chain, ChainDepositWithdrawHis tx) {
        log.info("executeWithdrawOneInternal chain={} tx={}", chain, tx);
        // 用户从冻结账户减去 提币金额
        boolean result1 = walletAccountService.updateFreeze(tx.getUserId(), tx.getCurrency(), tx.getAmount().negate(), true);
        Utils.check(result1, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);
        // 提币手续费账户增加 fee
        if (tx.getFeeAmount().signum() > 0) {
            boolean result2 = walletAccountService.updateAvailable(AccountConstants.getSystemWithdrawFeeUserId(), tx.getCurrency(), tx.getFeeAmount(), true);
            Utils.check(result2, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);
        }
        tx.setUpdateTime(System.currentTimeMillis());
        tx.setVersion(tx.getVersion() + 1);
        tx.setStatus(WithdrawStatus.TRANSACTION_CONFIRMED.getCode());
        log.info("executeWithdrawOneInternal update={}", tx);
        // 更新状态
        chainDepositWithdrawHisMapper.updateById(tx);
        // 标记财务记录提币成功
        userAccountActionService.updateStatusByActionUserIdSource(AccountAction.WITHDRAW, tx.getUserId(), String.valueOf(tx.getId()), CommonActionStatus.SUCCESS);

        // 处理收款方
        ChainDepositAddress assignedDepositAddress = chainDepositService.getByAddress(chain, tx.getToAddress());
        log.info("executeWithdrawOneInternal existDepositAddress={}", assignedDepositAddress);
        if (assignedDepositAddress != null) {
            BigDecimal amount = tx.getAmount().subtract(tx.getFeeAmount());
            // 获取币种的充币规则
            DepositRuleDto depositRuleDto = chainDepositService.getDepositRule(chain, tx.getCurrency());
            // 判断需要审核的3种情况： 1充提规则不存在，2充币规则禁用，3超过充币额度
            boolean requireReview = depositRuleDto == null || depositRuleDto.getStatus() == CommonStatus.DISABLED.getCode() || amount.compareTo(depositRuleDto.getAutoAmount()) > 0;

            if (requireReview) {
                // 需要运营人员审核就标记manual=1, status=PENDING_APPROVE, 并通知运维人员
                int manual = 1;
                int status = DepositStatus.PENDING_APPROVE.getCode();
                ChainDepositWithdrawHis depositHis = ChainDepositWithdrawHis.builder()
                        .createTime(System.currentTimeMillis())
                        .updateTime(System.currentTimeMillis())
                        .version(AccountConstants.DEFAULT_VERSION)
                        .userId(assignedDepositAddress.getUserId())
                        .chain(chain)
                        .action(ChainAction.DEPOSIT)
                        .fromAddress("")
                        .toAddress("")
                        .txToAddress("")
                        .currency(tx.getCurrency())
                        .internal(1)
                        .amount(amount)
                        .feeAmount(BigDecimal.ZERO)
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
                log.info("insert new internal deposit transaction={}", depositHis);
                chainDepositWithdrawHisMapper.insert(depositHis);
                // 充币审核通知管理员
                kafkaProducer.send(KafkaTopic.TOPIC_ACCOUNT_AUDIT, JSONUtil.toJsonStr(NotificationReq.builder()
                        .notificationType(NoticeTypeEnum.ACCOUNT_AUDIT.getCode())
                        .userSource(TargetSource.ADMIN.name())
                        .values(ImmutableMap.of(
                                "ts", System.currentTimeMillis(),
                                "type", AccountAction.DEPOSIT.getNotificationType()))
                        .build()));
                log.info("send audit notification, ts:{}, type:{}", System.currentTimeMillis(), AccountAction.DEPOSIT.getNotificationType());
            } else {
                // 如果不需要审核就 manual=0, status=TRANSACTION_CONFIRMED,上账，给用户发充币成功通知
                int manual = 0;
                int status = DepositStatus.TRANSACTION_CONFIRMED.getCode();
                ChainDepositWithdrawHis depositHis = ChainDepositWithdrawHis.builder()
                        .createTime(System.currentTimeMillis())
                        .updateTime(System.currentTimeMillis())
                        .version(AccountConstants.DEFAULT_VERSION)
                        .userId(assignedDepositAddress.getUserId())
                        .chain(chain)
                        .action(ChainAction.DEPOSIT)
                        .fromAddress("")
                        .toAddress("")
                        .txToAddress("")
                        .currency(tx.getCurrency())
                        .internal(1)
                        .amount(amount)
                        .feeAmount(BigDecimal.ZERO)
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
                log.info("insert new internal deposit transaction={}", depositHis);
                chainDepositWithdrawHisMapper.insert(depositHis);

                // 用户资产上账
                boolean result = walletAccountService.updateAvailable(assignedDepositAddress.getUserId(), tx.getCurrency(), amount, true);
                Utils.check(result, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);

                transactionService.afterCommit(() -> {
                    // 记录用户财务历史
                    userAccountActionService.createHistory(assignedDepositAddress.getUserId(), tx.getCurrency(), AccountAction.DEPOSIT, amount);

                    List<UserAccount> accounts = walletAccountService.getAccounts(tx.getUserId());
                    // 通知账户变更
                    kafkaProducer.send(KafkaTopic.TOPIC_ACCOUNT_UPDATE, JSONUtil.toJsonStr(NotificationReq.builder()
                            .notificationType(NoticeTypeEnum.ACCOUNT_BALANCE_CHANGE.getCode())
                            .userSource(TargetSource.UC.name())
                            .ucUserIds(ImmutableList.of(tx.getUserId()))
                            .values(ImmutableMap.of(
                                    "ts", System.currentTimeMillis(),
                                    "currency", tx.getCurrency(),
                                    "change", tx.getAmount(),
                                    "after", CollectionUtils.isEmpty(accounts) ? "" : accounts.get(0).getAvailable()))
                            .build()));
                    log.info("send balance change notification userid:{}, ts:{},currency:{},change:{}", tx.getUserId(), System.currentTimeMillis(), tx.getCurrency(), tx.getAmount());


                    // 发送通知给客户(充币方)
                    kafkaProducer.send(KafkaTopic.TOPIC_ACCOUNT_UPDATE, JSONUtil.toJsonStr(NotificationReq.builder()
                            .notificationType(NoticeTypeEnum.ACCOUNT_DEPOSIT.getCode())
                            .userSource(TargetSource.UC.name())
                            .ucUserIds(ImmutableList.of(assignedDepositAddress.getUserId()))
                            .values(ImmutableMap.of(
                                    "ts", System.currentTimeMillis(),
                                    "currency", tx.getCurrency(),
                                    "amount", amount))
                            .build()));
                    log.info("send internal deposit notification userID:{}, ts:{},currency:{},amount:{}", assignedDepositAddress.getUserId(), System.currentTimeMillis(), tx.getCurrency(), amount);

                });
            }
        }

        List<UserAccount> accounts = walletAccountService.getAccounts(tx.getUserId());
        // 通知账户变更
        kafkaProducer.send(KafkaTopic.TOPIC_ACCOUNT_UPDATE, JSONUtil.toJsonStr(NotificationReq.builder()
                .notificationType(NoticeTypeEnum.ACCOUNT_BALANCE_CHANGE.getCode())
                .userSource(TargetSource.UC.name())
                .ucUserIds(ImmutableList.of(tx.getUserId()))
                .values(ImmutableMap.of(
                        "ts", System.currentTimeMillis(),
                        "currency", tx.getCurrency(),
                        "change", tx.getAmount(),
                        "after", CollectionUtils.isEmpty(accounts) ? "" : accounts.get(0).getAvailable()))
                .build()));
        log.info("send balance change notification userid:{}, ts:{},currency:{},change:{}", tx.getUserId(), System.currentTimeMillis(), tx.getCurrency(), tx.getAmount());



        // 发送通知用户提示提币成功(提币方)
        kafkaProducer.sendAsync(KafkaTopic.TOPIC_ACCOUNT_UPDATE, JSONUtil.toJsonStr(NotificationReq.builder()
                .notificationType(NoticeTypeEnum.ACCOUNT_WITHDRAW.getCode())
                .userSource(TargetSource.UC.name())
                .ucUserIds(ImmutableList.of(tx.getUserId()))
                .values(ImmutableMap.of(
                        "ts", System.currentTimeMillis(),
                        "currency", tx.getCurrency(),
                        "amount", tx.getAmount()))
                .build()));
        log.info("send withdraw notification ts:{},currency:{},amount:{}", System.currentTimeMillis(), tx.getCurrency(), tx.getAmount());
    }


    /**
     * DEFI提币，只是更改一下状态, 并不处理冻结资产
     *
     * @param chain defi-chain
     * @param tx
     * @throws Exception
     */
    private void executeWithdrawOneOnChain(Chain chain, ChainDepositWithdrawHis tx) throws Exception {
        log.info("executeWithdrawOneOnChain chain={} tx={}", chain, tx);
        tx.setUpdateTime(System.currentTimeMillis());
        tx.setVersion(tx.getVersion() + 1);
        tx.setStatus(WithdrawStatus.TRANSACTION_ON_CHAIN.getCode());
        // 更新状态
        log.info("executeWithdrawOneOnChain update={}", tx);
        chainDepositWithdrawHisMapper.updateById(tx);
    }

    private int getWithdrawBatch() {
        return Integer.parseInt(systemConfigService.getValue(AccountSystemConfig.CHAIN_WITHDRAW_BATCH, "20"));
    }

    private void scanBlock(Chain chain) throws Exception {
        boolean enabled = actionControlService.isEnabled("chain", String.format("scan_block_%s", chain.getName().toLowerCase()));
        if (!enabled) {
            return;
        }
        // 获取数据库中当前链记录的最后一个块
        ChainBlock chainBlock = chainBlockMapper.getLatestBlock(chain);
        if (chainBlock == null) {
            return;
        }

        // 查询此链支持的DEFI充提的信息
//        Chain defiChain = Chain.supportedDefiChain(chain);

        // 读取这个块链上的信息用于判断链是否回滚
        NativeChainBlockDto nativeChainBlockDto = chainService.getChainNativeBlock(chain, chainBlock.getBlockNumber());
        if (nativeChainBlockDto == null) {
            log.info("rollback block as can not find chainBlock={}", chainBlock);
            // 回滚链提币
            rollbackBlock(chain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
            // 回滚DEFI链提币
//            if (defiChain != null) {
//                rollbackBlock(defiChain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
//            }
            return;
        }
        Assert.isTrue(nativeChainBlockDto.getChain() != null, "missing chain info");

        if (!ObjectUtils.isAddressEquals(nativeChainBlockDto.getBlockHash(), chainBlock.getBlockHash())) {
            // block hash not identical
            log.info("rollback block as block hash changed db-block={} native-chain-block={}", chainBlock, nativeChainBlockDto);
            rollbackBlock(chain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
//            if (defiChain != null) {
//                rollbackBlock(defiChain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
//            }
            return;
        }
        long nextBlockNumber = chainBlock.getBlockNumber() + 1;
        long chainBlockNumber = chainService.getLatestBlockNumber(chain);
        // not to read the highest block as it's the most rollback often
        if (nextBlockNumber >= chainBlockNumber) {
            return;
        }

        long pendingBlocks = chainBlockNumber - chainBlock.getBlockNumber();
//        log.info("scanBlock chain={} defi-chain={} my-last-block-number={} chain-last-block-number={} pendingBlocks={}", chain, defiChain, chainBlock.getBlockNumber(), chainBlockNumber, pendingBlocks);
        log.info("scanBlock chain={} defi-chain={} my-last-block-number={} chain-last-block-number={} pendingBlocks={}", chain, null, chainBlock.getBlockNumber(), chainBlockNumber, pendingBlocks);

        // 使用指标记录一下
        Metrics.summary("chain-pending-blocks", Tags.of("chain", chain.getName())).record(pendingBlocks);

        // 读取链上下一个块
        NativeChainBlockDto newNativeChainBlockDto = chainService.getChainNativeBlock(chain, nextBlockNumber);
        if (newNativeChainBlockDto == null) {
            return;
        }
        log.info("scanBlock newBlockDto={}", newNativeChainBlockDto);
        if (!ObjectUtils.isAddressEquals(chainBlock.getBlockHash(), newNativeChainBlockDto.getParentHash())) {
            // new block's parent not link to the block in db
            log.info("rollback block as block parent hash changed db-block={} new-chain-block={}", chainBlock, newNativeChainBlockDto);
            rollbackBlock(chain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
//            if (defiChain != null) {
//                rollbackBlock(defiChain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
//            }
            return;
        }

        // 获取绑定用户此链上的地址 (需要兼容BSC使用了ETH的地址)
        List<ChainDepositAddress> assignedList = chainDepositAddressMapper.getAssignedAddresses(Chain.mapChain(chain).getCode());
        // 获取绑定用户的地址
        List<String> assignedAddresses = assignedList.size() == 0
                ? Lists.newArrayList()
                : assignedList.stream().map(ChainDepositAddress::getAddress).collect(Collectors.toList());

        // 获取新块的Deposit交易（链上）
        List<NativeChainTransactionDto> transactions = assignedAddresses.size() == 0
                ? Lists.newArrayList()
//                : chainService.getTransactions(chain, defiChain, newNativeChainBlockDto.getBlockNumber(), assignedAddresses);
                : chainService.getTransactions(chain, null, newNativeChainBlockDto.getBlockNumber(), assignedAddresses);

        if (transactions.size() > 0) {
            // 处理充币精度
            transactions.forEach(e -> e.setAmount(e.getAmount().setScale(DEPOSIT_DECIMALS, RoundingMode.DOWN)));
            // 过滤掉数据太小的
            // 过滤掉没有充币规则或者充币禁用的
            transactions = transactions.stream().filter(this::allowDeposit).collect(Collectors.toList());
        }

        if (transactions.size() > 0) {
            // 读取系统钱包地址
            List<SystemWalletAddressDto> systemWalletAddresses = systemWalletAddressService.getAll();
            // 从系统热钱包地址打币不能给用户上账 milo 2021-02-04
            transactions = transactions.stream()
                    .filter(e -> systemWalletAddresses.stream().noneMatch(s -> s.getType() == WalletAddressType.HOT.getCode() && ObjectUtils.isAddressEquals(e.getFromAddress(), s.getAddress())))
                    .collect(Collectors.toList());
        }

        long canConfirmBlockNumber = nextBlockNumber - chainService.getConfirmBlocks(chain);
        // 获取等待确认的Deposit（数据库）
        List<ChainDepositWithdrawHis> pendingConfirmList = Lists.newArrayList();
        pendingConfirmList.addAll(chainDepositWithdrawHisMapper.getChainDepositWithdrawEarlierThanBlockNumber(chain, ChainAction.DEPOSIT.getCode(), DepositStatus.CREATED.getCode(), canConfirmBlockNumber));
//        if (defiChain != null) {
//            pendingConfirmList.addAll(chainDepositWithdrawHisMapper.getChainDepositWithdrawEarlierThanBlockNumber(defiChain, ChainAction.DEPOSIT.getCode(), DepositStatus.CREATED.getCode(), canConfirmBlockNumber));
//        }

        // 读取充币黑地址列表，如果没有新的deposit就没必要读取
        List<String> blacklistAddresses = pendingConfirmList.size() == 0
                ? Lists.newArrayList()
                : blacklistAddressService.getAll()
                .stream()
                .filter(e -> e.getType() == ChainAction.DEPOSIT.getCode() && e.getStatus() == CommonStatus.ENABLED)
                .map(BlacklistAddressDto::getAddress)
                .collect(Collectors.toList());

        // 黑名单的需要标记(针对fromAddress)
        List<ChainDepositWithdrawHis> toSuspendedList = blacklistAddresses.size() == 0
                ? Lists.newArrayList()
                : pendingConfirmList.stream().filter(o -> anyMatch(blacklistAddresses, o.getFromAddress())).collect(Collectors.toList());

        // 不在黑名单的可以确认(针对fromAddress)
        List<ChainDepositWithdrawHis> toConfirmList = blacklistAddresses.size() == 0
                ? pendingConfirmList
                : pendingConfirmList.stream().filter(o -> !anyMatch(blacklistAddresses, o.getFromAddress())).collect(Collectors.toList());

        // 进行数据库事务操作
        chainActionPersistentService.processNewBlock(newNativeChainBlockDto, assignedList, transactions, toSuspendedList, toConfirmList);
    }

    private boolean anyMatch(List<String> blacklistAddresses, String address) {
        return blacklistAddresses.stream().anyMatch(o -> ObjectUtils.isAddressEquals(o, address));
    }

    /**
     * 回滚块
     *
     * @param chain
     * @param blockNumber
     * @param blockHash
     */
    private void rollbackBlock(Chain chain, long blockNumber, String blockHash) {
        chainActionPersistentService.rollbackBlock(chain, blockNumber, blockHash);
    }

    /**
     * 判断是否允许充币
     *
     * @param e
     * @return
     */
    private boolean allowDeposit(NativeChainTransactionDto e) {
        if (e.getAmount().signum() > 0) {
            DepositRuleDto rule = chainDepositService.getDepositRule(e.getChain(), e.getCurrency());
            return rule != null && rule.getStatus() == CommonStatus.ENABLED.getCode();
        }
        return false;
    }

    @Override
    @SingletonLock(key = "/seeds/account/chain/scan-withdraw")
    public void scanWithdraw() throws Exception {
        boolean enabled = actionControlService.isEnabled(AccountActionControl.CHAIN_WITHDRAW);
        if (!enabled) {
            return;
        }
        // 先处理 replace case
        scanTxnReplace(ChainTxnReplaceAppType.WITHDRAW);

        List<ChainDepositWithdrawHis> list = chainDepositWithdrawHisMapper.getListByStatus(
                ChainAction.WITHDRAW.getCode(),
                Lists.newArrayList(WithdrawStatus.TRANSACTION_ON_CHAIN.getCode()));
        if (list.size() == 0) {
            return;
        }
        // 限制每次处理的提币请求个数
        int watchWithdrawBatch = getWithdrawBatch();
        for (int i = 0; i < list.size() && i < watchWithdrawBatch; i++) {
            ChainDepositWithdrawHis tx = list.get(i);
            Chain chain = tx.getChain();
            // 用事务包住单个的提币，防止由于单个错误导致的回滚
            try {
                ChainBlock latestChainBlock = chainBlockMapper.getLatestBlock(chain);
                if (latestChainBlock != null) {
                    transactionService.execute(() -> {
                        scanWithdrawOne(chain, latestChainBlock, tx);
                        return null;
                    });
                }
            } catch (Exception e) {
                log.error("scanWithdraw chain={} tx={}", chain, tx, e);
            }
        }
    }

    @Override
    public Map<Chain, ChainBlock> getLatestBlock(List<Chain> chainList) {
        Map<Chain, ChainBlock> latestBlockMap = new ConcurrentHashMap<>();
        chainList.stream().distinct().forEach(chain -> {
            ChainBlock latestChainBlock = chainBlockMapper.getLatestBlock(chain);
            if (latestChainBlock == null) {
                return;
            }
            latestBlockMap.put(chain, latestChainBlock);
        });
        return latestBlockMap;
    }

    @Override
    @SingletonLock(key = "/seeds/account/chain/replay")
    @Transactional(rollbackFor = Exception.class)
    public Boolean replayTransaction(ChainTxnReplayDto chainTxnReplayDto) throws Exception {
        log.info("replayTransaction, dto={}", chainTxnReplayDto);
        ChainTxnReplaceAppType appType = ChainTxnReplaceAppType.fromCode(chainTxnReplayDto.getType());
        switch (appType) {
            case WITHDRAW:
                ChainDepositWithdrawHis withdrawHis = chainDepositWithdrawHisService.getById(chainTxnReplayDto.getId());
                if (withdrawHis == null || (!WithdrawStatus.TRANSACTION_ON_CHAIN.getCode().equals(withdrawHis.getStatus()) &&
                        !WithdrawStatus.TRANSACTION_FAILED_PENDING_CHECK.getCode().equals(withdrawHis.getStatus())) ||
                        !withdrawHis.getTxHash().equalsIgnoreCase(chainTxnReplayDto.getTxHash())) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn");
                }
                if (System.currentTimeMillis() - withdrawHis.getUpdateTime() < TimeUnit.SECONDS.toMillis(1)) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "not allowed duplicate replay");
                }
                ChainTxnData chainTxnData = chainTxnDataMapper.selectByAppTypeAndId(
                        ChainTxnDataAppType.WITHDRAW,
                        withdrawHis.getId()
                );
                if (chainTxnData == null && !WithdrawStatus.TRANSACTION_FAILED_PENDING_CHECK.getCode().equals(withdrawHis.getStatus())) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn data");
                }
                return execute(withdrawHis, chainTxnData) > 0;

            case HOT_WALLET_TRANSFER:
                AddressCollectHis collectHis = addressCollectHisMapper.selectByPrimaryKey(chainTxnReplayDto.getId());
                if (collectHis == null || !ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode().equals(collectHis.getStatus()) ||
                        !collectHis.getTxHash().equalsIgnoreCase(chainTxnReplayDto.getTxHash())) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn");
                }
                if (System.currentTimeMillis() - collectHis.getUpdateTime() < TimeUnit.SECONDS.toMillis(1)) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "not allowed duplicate replay");
                }
                chainTxnData = chainTxnDataMapper.selectByAppTypeAndId(
                        ChainTxnDataAppType.HOT_WALLET_TRANSFER,
                        collectHis.getId()
                );
                if (chainTxnData == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn data");
                }

                return execute(collectHis, chainTxnData.getData()) > 0;
            case CASH_COLLECT:
                collectHis = addressCollectHisMapper.selectByPrimaryKey(chainTxnReplayDto.getId());
                if (collectHis == null || !ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode().equals(collectHis.getStatus()) ||
                        !collectHis.getTxHash().equalsIgnoreCase(chainTxnReplayDto.getTxHash())) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn");
                }
                if (System.currentTimeMillis() - collectHis.getUpdateTime() < TimeUnit.SECONDS.toMillis(1)) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "not allowed duplicate replay");
                }
                chainTxnData = chainTxnDataMapper.selectByAppTypeAndId(
                        ChainTxnDataAppType.CASH_COLLECT,
                        collectHis.getId()
                );
                if (chainTxnData == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn data");
                }

                return execute(collectHis, chainTxnData.getData()) > 0;
            case GAS_TRANSFER:
                collectHis = addressCollectHisMapper.selectByPrimaryKey(chainTxnReplayDto.getId());
                if (collectHis == null || !ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode().equals(collectHis.getStatus()) ||
                        !collectHis.getTxHash().equalsIgnoreCase(chainTxnReplayDto.getTxHash())) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn");
                }
                if (System.currentTimeMillis() - collectHis.getUpdateTime() < TimeUnit.SECONDS.toMillis(1)) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "not allowed duplicate replay");
                }
                chainTxnData = chainTxnDataMapper.selectByAppTypeAndId(
                        ChainTxnDataAppType.GAS_TRANSFER,
                        collectHis.getId()
                );
                if (chainTxnData == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn data");
                }

                return execute(collectHis, chainTxnData.getData()) > 0;

            default:
                throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "unknown chainTxnReplaceAppType");
        }
    }

    @Override
    public IPage<ChainTxnDto> getTxnList(ChainTxnPageReq req) {
        Map<Chain, Long> chainExpireMap = getChainExpireTime();
        Chain chain = Chain.fromCode(req.getChain());
        BigInteger chainGasPrice = getChainGasPrice(chain);
        long expireTimestamp = System.currentTimeMillis() - (chainExpireMap.containsKey(chain) ?
                TimeUnit.SECONDS.toMillis(chainExpireMap.get(chain)) : 0);
        Integer status = req.getStatus();
        Page page = new Page();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());
        Integer chainStatus;
        IPage<AddressCollectHis> collectPage;
        switch (req.getType()) {

            case 1:
                List<Integer> statusList;
                // 提币
                if (status == 1) {
                    statusList = Arrays.asList(WithdrawStatus.TRANSACTION_ON_CHAIN.getCode(), WithdrawStatus.TRANSACTION_FAILED_PENDING_CHECK.getCode());
                } else if (status == 6) {
                    statusList = Collections.singletonList(WithdrawStatus.TRANSACTION_CONFIRMED.getCode());
                } else {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "invalid status");
                }
                IPage<ChainDepositWithdrawHis> withdrawPage = chainDepositWithdrawHisService.selectByChainStatusAndTimestamp(page, chain, statusList, ChainAction.WITHDRAW, expireTimestamp);
                if (CollectionUtils.isEmpty(withdrawPage.getRecords())) {
                    return withdrawPage.convert(p -> null);
                }
                return withdrawPage.convert(p -> convert2Dto(p, chainGasPrice, 1));

            case 2:
                // 热钱包划转
                if (status == 1) {
                    chainStatus = ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode();
                } else if (status == 6) {
                    chainStatus = ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED.getCode();
                } else {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "invalid status");
                }
                collectPage = addressCollectHisMapper.getHotWalletByChainStatusAndTimestamp(page, chain, chainStatus, expireTimestamp);
                if (CollectionUtils.isEmpty(collectPage.getRecords())) {
                    return collectPage.convert(p -> null);
                }
                return collectPage.convert(p -> convert2Dto(p, chainGasPrice, 2));
            case 3:
                // 资金归集
                if (status == 1) {
                    chainStatus = ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode();
                } else if (status == 6) {
                    chainStatus = ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED.getCode();
                } else {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "invalid status");
                }
                collectPage = addressCollectHisMapper.getByChainStatusOrderTypeAndTimestamp(page, chain, chainStatus, FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode(), expireTimestamp);
                if (CollectionUtils.isEmpty(collectPage.getRecords())) {
                    return collectPage.convert(p -> null);
                }
                return collectPage.convert(p -> convert2Dto(p, chainGasPrice, 3));
            case 4:
                // Gas划转
                if (status == 1) {
                    chainStatus = ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode();
                } else if (status == 6) {
                    chainStatus = ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED.getCode();
                } else {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "invalid status");
                }
                collectPage = addressCollectHisMapper.getByChainStatusOrderTypeAndTimestamp(page, chain, chainStatus, FundCollectOrderType.FROM_SYSTEM_TO_USER.getCode(), expireTimestamp);
                if (CollectionUtils.isEmpty(collectPage.getRecords())) {
                    return collectPage.convert(p -> null);
                }
                return collectPage.convert(p -> convert2Dto(p, chainGasPrice, 4));
            default:
                throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "unknown type");
        }
    }

    @Override
    public IPage<ChainTxnDto> getTxnReplaceList(ChainTxnPageReq req) {
        Page page = new Page();
        page.setCurrent(req.getCurrent());
        page.setSize(req.getSize());
        IPage<ChainTxnReplace> replacePage = chainTxnReplaceMapper.getListByChainStatusAndType(page, Chain.fromCode(req.getChain()),
                ChainCommonStatus.fromCode(req.getStatus()), ChainTxnReplaceAppType.fromCode(req.getType()));
        if (CollectionUtils.isEmpty(replacePage.getRecords())) {
            return replacePage.convert(p -> null);
        }
        return replacePage.convert(this::convert2Dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long replaceTransaction(ChainTxnReplaceDto chainTxnReplaceDto) throws Exception  {
        log.info("replaceTransaction, dto={}", chainTxnReplaceDto);
        ChainTxnReplaceAppType appType = ChainTxnReplaceAppType.fromCode(chainTxnReplaceDto.getType());
        Chain chain = Chain.fromCode(chainTxnReplaceDto.getChain());
        switch (appType) {

            case WITHDRAW:
                ChainDepositWithdrawHis withdrawHis = chainDepositWithdrawHisMapper.getByChainHash(chain, chainTxnReplaceDto.getTxHash());
                if (withdrawHis == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn");
                }
                ChainTxnData chainTxnData = chainTxnDataMapper.selectByAppTypeAndId(
                        ChainTxnDataAppType.WITHDRAW,
                        withdrawHis.getId()
                );
                if (chainTxnData == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn data");
                }

                return execute(withdrawHis.getChain(), withdrawHis.getId(), appType, withdrawHis.getTxHash(), withdrawHis.getTxValue(), withdrawHis.getFromAddress(),
                        withdrawHis.getTxToAddress(), withdrawHis.getNonce(),
                        chainTxnReplaceDto.getGasPrice(), chainTxnReplaceDto.getGasLimit(), chainTxnData.getData());

            case HOT_WALLET_TRANSFER:
                AddressCollectHis collectHis = addressCollectHisMapper.getByChainHash(chain, chainTxnReplaceDto.getTxHash());
                if (collectHis == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn");
                }
                chainTxnData = chainTxnDataMapper.selectByAppTypeAndId(
                        ChainTxnDataAppType.HOT_WALLET_TRANSFER,
                        collectHis.getId()
                );
                if (chainTxnData == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn data");
                }

                return execute(collectHis.getChain(), collectHis.getId(), appType, collectHis.getTxHash(), collectHis.getTxValue(), collectHis.getFromAddress(),
                        collectHis.getTxToAddress(), collectHis.getNonce(),
                        chainTxnReplaceDto.getGasPrice(), chainTxnReplaceDto.getGasLimit(), chainTxnData.getData());

            case CASH_COLLECT:
                collectHis = addressCollectHisMapper.getByChainHash(chain, chainTxnReplaceDto.getTxHash());
                if (collectHis == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn");
                }
                chainTxnData = chainTxnDataMapper.selectByAppTypeAndId(
                        ChainTxnDataAppType.CASH_COLLECT,
                        collectHis.getId()
                );
                if (chainTxnData == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn data");
                }

                return execute(collectHis.getChain(), collectHis.getId(), appType, collectHis.getTxHash(), collectHis.getTxValue(), collectHis.getFromAddress(),
                        collectHis.getTxToAddress(), collectHis.getNonce(),
                        chainTxnReplaceDto.getGasPrice(), chainTxnReplaceDto.getGasLimit(), chainTxnData.getData());

            case GAS_TRANSFER:
                collectHis = addressCollectHisMapper.getByChainHash(chain, chainTxnReplaceDto.getTxHash());
                if (collectHis == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn");
                }
                chainTxnData = chainTxnDataMapper.selectByAppTypeAndId(
                        ChainTxnDataAppType.GAS_TRANSFER,
                        collectHis.getId()
                );
                if (chainTxnData == null) {
                    throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "can not find the txn data");
                }

                return execute(collectHis.getChain(), collectHis.getId(), appType, collectHis.getTxHash(), collectHis.getTxValue(), collectHis.getFromAddress(),
                        collectHis.getTxToAddress(), collectHis.getNonce(),
                        chainTxnReplaceDto.getGasPrice(), chainTxnReplaceDto.getGasLimit(), chainTxnData.getData());

            default:
                throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "unknown chainTxnReplaceAppType");
        }
    }

    @Override
    public void scanTxnReplace(ChainTxnReplaceAppType replaceAppType) throws Exception {
        List<ChainTxnReplace> list = chainTxnReplaceMapper.getListByStatusAndType(
                Arrays.asList(ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode(), ChainCommonStatus.TRANSACTION_PENDING_CONFIRMED.getCode()),
                replaceAppType
        );
        Map<Chain, ChainBlock> latestBlockMap = getLatestBlock(list.stream().distinct().map(ChainTxnReplace::getChain).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latestBlockMap)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                ChainTxnReplace tx = list.get(i);
                ChainTransactionReceipt receipt = chainService.getTransactionReceiptByTxHash(tx.getChain(), tx.getTxHash());
                transactionService.execute(() -> {
                    if (receipt != null) {
                        ChainTransactionReceipt result = receipt;
                        if (result.getStatus()) {
                            long blockNumber = result.getBlockNumber().longValue();
                            if (tx.getAppType().equals(ChainTxnReplaceAppType.EXCHANGE_SYSTEM_HIS) ||
                                    tx.getAppType().equals(ChainTxnReplaceAppType.EXCHANGE_DIVIDEND_HIS)) {
                                ChainExchangeHis exchangeHis = chainExchangeHisMapper.selectByPrimaryKey(tx.getAppId());
                                processUnSafeConfirmedExchangeHis(exchangeHis, result);
                                chainExchangeHisMapper.updateByPrimaryKey(exchangeHis);
                            }
                            // 要等n个块确认才行
                            if (latestBlockMap.containsKey(tx.getChain()) &&
                                    latestBlockMap.get(tx.getChain()).getBlockNumber() - blockNumber > chainService.getConfirmBlocks(tx.getChain())) {
                                String blockHash = result.getBlockHash();
                                long gasUsed = result.getGasUsed().longValue();
                                // 计算gas花费
                                BigDecimal txFee = chainService.calcTxFee(tx.getChain(), gasUsed, tx.getGasPrice());

                                tx.setUpdateTime(System.currentTimeMillis());
                                tx.setVersion(tx.getVersion() + 1);
                                tx.setBlockNumber(blockNumber);
                                tx.setBlockHash(blockHash);
                                tx.setTxFee(txFee);
                                tx.setGasUsed(gasUsed);
                                tx.setStatus(ChainCommonStatus.TRANSACTION_CONFIRMED);

                                switch (tx.getAppType()) {
                                    case WITHDRAW:
                                        ChainDepositWithdrawHis withdrawHis = chainDepositWithdrawHisMapper.selectById(tx.getAppId());
                                        if (withdrawHis == null) {
                                            log.error("chain replacement, failed to find the origin txn in db, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                            // 发送通知给运维介入
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " original withdraw tx is null"))
//                                                    .build());
                                        } else if (ChainCommonStatus.TRANSACTION_CONFIRMED.getCode().equals(withdrawHis.getStatus())) {
                                            log.error("chain replacement, conflict with origin txn, both confirmed by chain, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                            // 发送通知给运维介入
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " withdraw tx conflict"))
//                                                    .build());
                                        } else {
                                            // 更新状态
                                            chainTxnReplaceMapper.updateByPrimaryKey(tx);
//                                            // 确认别的replace标为失败
//                                            cancelOtherReplaceList(tx);
                                            // 更新原始tx状态
                                            updateWithdrawTxn(withdrawHis, tx, 0L, "", BigDecimal.ZERO, true);

                                            // 发送通知mgt用户提示tx replace成功
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", "succeed"))
//                                                    .build());
                                            log.info("chain replacement, success to replace the origin txn, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                        }
                                        break;
                                    case HOT_WALLET_TRANSFER:
                                    case CASH_COLLECT:
                                    case GAS_TRANSFER:
                                        AddressCollectHis walletHis = addressCollectHisMapper.selectByPrimaryKey(tx.getAppId());
                                        if (walletHis == null) {
                                            log.error("chain replacement, failed to find the origin txn in db, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                            // 发送通知给运维介入
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " original wallet transfer/collect/gas tx is null"))
//                                                    .build());
                                        } else if (ChainCommonStatus.TRANSACTION_CONFIRMED.getCode().equals(walletHis.getStatus()) ||
                                                ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED.getCode().equals(walletHis.getStatus())) {
                                            log.error("chain replacement, conflict with origin txn, both confirmed by chain, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                            // 发送通知给运维介入
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " conflict"))
//                                                    .build());
                                        } else {
                                            // 更新状态
                                            chainTxnReplaceMapper.updateByPrimaryKey(tx);
                                            // 更新原始tx状态
                                            processCollectSuccess(walletHis, 0L, "", BigDecimal.ZERO, true);
                                            // 确认别的标为失败
//                                            cancelOtherReplaceList(tx);
                                            // 发送通知mgt用户提示tx replace成功
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", "succeed"))
//                                                    .build());
                                            log.info("chain replacement, success to replace the origin txn, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                        }
                                        break;
                                    case EXCHANGE_SYSTEM_HIS:
                                    case EXCHANGE_DIVIDEND_HIS:
                                        ChainExchangeHis exchangeHis = chainExchangeHisMapper.selectByPrimaryKey(tx.getAppId());
                                        if (exchangeHis == null) {
                                            log.error("chain replacement, failed to find the origin txn in db, appId={}, appType={}", tx.getAppId(), tx.getAppType());
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " original exchange tx is null"))
//                                                    .build());
                                        } else if (ChainCommonStatus.TRANSACTION_CONFIRMED.equals(exchangeHis.getStatus()) ||
                                                ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED.equals(exchangeHis.getStatus())) {
                                            log.error("chain replacement, conflict with origin txn, both confirmed by chain, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                            // 发送通知给运维介入
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " conflict"))
//                                                    .build());
                                        } else {
                                            // 更新状态
                                            chainTxnReplaceMapper.updateByPrimaryKey(tx);
                                            // 更新原始tx状态
                                            processExchangeSuccess(exchangeHis, 0L, "", BigDecimal.ZERO, 0L, true);
                                            // 确认别的标为失败
//                                            cancelOtherReplaceList(tx);
                                            // 发送通知mgt用户提示tx replace成功
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", "succeed"))
//                                                    .build());
                                            log.info("chain replacement, success to replace the origin txn, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                        }
                                        break;
                                    case DIVIDEND_HIS:
                                        ChainDividendHis dividendHis = chainDividendHisMapper.selectByPrimaryKey(tx.getAppId());
                                        if (dividendHis == null) {
                                            log.error("chain replacement, failed to find the origin txn in db, appId={}, appType={}", tx.getAppId(), tx.getAppType());
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " original dividend tx is null"))
//                                                    .build());
                                        } else if (ChainCommonStatus.TRANSACTION_CONFIRMED.equals(dividendHis.getStatus()) ||
                                                ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED.equals(dividendHis.getStatus())) {
                                            log.error("chain replacement, conflict with origin txn, both confirmed by chain, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                            // 发送通知给运维介入
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " conflict"))
//                                                    .build());
                                        } else {
                                            // 更新状态
                                            chainTxnReplaceMapper.updateByPrimaryKey(tx);
                                            // 更新原始tx
                                            processDividendSuccess(dividendHis, 0L, "", BigDecimal.ZERO, 0L, true);
                                            // 确认别的标为失败
//                                            cancelOtherReplaceList(tx);
                                            // 发送通知mgt用户提示tx replace成功
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", "succeed"))
//                                                    .build());
                                            log.info("chain replacement, success to replace the origin txn, appId={}, appType={}", tx.getAppId(), tx.getAppType());
                                        }
                                        break;
//                                    case MCD_HIS:
//                                        McdHis mcdHis = mcdHisMapper.selectByPrimaryKey(tx.getAppId());
//                                        if (mcdHis == null) {
//                                            log.error("chain replacement, failed to find the origin txn in db, appId={}, appType={}", tx.getAppId(), tx.getAppType());
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " original mcd tx is null"))
//                                                    .build());
//                                        } else if (ChainMcdStatus.TRANSACTION_CONFIRMED.equals(mcdHis.getStatus()) ||
//                                                ChainMcdStatus.TRANSACTION_CANCELLED_AND_REPLACED.equals(mcdHis.getStatus())) {
//                                            log.error("chain replacement, conflict with origin txn, both confirmed by chain, appId={}, appType={}", tx.getAppId(), tx.getAppType());
//                                            // 发送通知给运维介入
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", tx.getId() + " conflict"))
//                                                    .build());
//                                        } else {
//                                            // 更新状态
//                                            chainTxnReplaceMapper.updateByPrimaryKey(tx);
//                                            // 更新原始tx
//                                            processMcdSuccess(mcdHis, 0L, "", BigDecimal.ZERO, 0L, true);
//                                            // 确认别的标为失败
////                                            cancelOtherReplaceList(tx);
//                                            // 发送通知mgt用户提示tx replace成功
//                                            notificationService.sendNotificationAsync(NotificationDto.builder()
//                                                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                                    .values(ImmutableMap.of(
//                                                            "ts", System.currentTimeMillis(),
//                                                            "module", "transaction-replacement",
//                                                            "content", "succeed"))
//                                                    .build());
//                                            log.info("chain replacement, success to replace the origin txn, appId={}, appType={}", tx.getAppId(), tx.getAppType());
//                                        }
//                                        break;
                                    default:
                                        throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, String.format("unknown tx appType=%s", tx.getAppType()));
                                }
                            } else {
                                // waiting for confirm
                            }
                        } else {
                            // 超过安全确认，再标为失败
                            if (latestBlockMap.containsKey(tx.getChain()) &&
                                    latestBlockMap.get(tx.getChain()).getBlockNumber() - result.getBlockNumber().longValue() > chainService.getConfirmBlocks(tx.getChain())) {
                                // 更新状态为失败
                                chainTxnReplaceMapper.updateStatusByPrimaryKey(tx.getId(), ChainCommonStatus.TRANSACTION_FAILED);
                                // 发送通知给运维介入
//                                notificationService.sendNotificationAsync(NotificationDto.builder()
//                                        .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                                        .values(ImmutableMap.of(
//                                                "ts", System.currentTimeMillis(),
//                                                "module", "transaction-replacement",
//                                                "content", tx.getId() + " failed"))
//                                        .build());
                            } else {
                                // wait to confirm
                            }

                        }
                    }
                    return null;
                });
            } catch (Exception e) {
                log.error("scanTxnReplace error, ", e);
            }
        }
    }

    private void processDividendSuccess(ChainDividendHis tx, long blockNumber, String blockHash, BigDecimal txFee, long gasUsed, boolean isReplace) {
        tx.setUpdateTime(System.currentTimeMillis());
        tx.setVersion(tx.getVersion() + 1);
        tx.setBlockNumber(blockNumber);
        tx.setBlockHash(blockHash);
        tx.setTxFee(txFee);
        tx.setGasUsed(gasUsed);
        tx.setStatus(isReplace ? ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED : ChainCommonStatus.TRANSACTION_CONFIRMED);
        // 更新状态
        chainDividendHisMapper.updateByPrimaryKey(tx);

        // 将链上分红账户freeze Kine减掉
        boolean isSuccess = walletAccountService.updateFreeze(tx.getUserId(), tx.getCurrency(), tx.getAmount().negate(), true);
        if (!isSuccess) {
            // 发送通知给运维介入
//            notificationService.sendNotificationAsync(NotificationDto.builder()
//                    .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                    .values(ImmutableMap.of(
//                            "ts", System.currentTimeMillis(),
//                            "module", "dividend",
//                            "content", tx.getId() + " freeze failed"))
//                    .build());
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR,
                    String.format("dividend, failed to reduce KINE freeze in chain dividend account, userId=%s, amount=%s",
                            tx.getUserId(), tx.getAmount()));
        }
        userAccountActionService.createHistory(tx.getUserId(), tx.getCurrency(),
                AccountAction.CHAIN_DIVIDEND_TRANSFER, tx.getAmount());

        // 发送通知mgt用户提示分红划转成功
//        notificationService.sendNotificationAsync(NotificationDto.builder()
//                .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                .values(ImmutableMap.of(
//                        "ts", System.currentTimeMillis(),
//                        "module", "dividend",
//                        "content", tx.getId() + " succeeded"))
//                .build());
    }
    
    private void scanWithdrawOne(Chain chain, ChainBlock latestChainBlock, ChainDepositWithdrawHis tx) throws Exception {
        ChainTransactionReceipt receipt = chainService.getTransactionReceiptByTxHash(chain, tx.getTxHash());
        if (receipt != null) {
            ChainTransactionReceipt result = receipt;
            if (result.getStatus()) {
                long blockNumber = result.getBlockNumber();
                // 要等n个块确认才行
                if (latestChainBlock.getBlockNumber() - blockNumber > chainService.getConfirmBlocks(chain)) {
                    String blockHash = result.getBlockHash();
                    long gasUsed = result.getGasUsed();
                    // 计算gas花费
                    BigDecimal txFee = chainService.calcTxFee(tx.getChain(), gasUsed, tx.getGasPrice());

                    updateWithdrawTxn(tx, null, blockNumber, blockHash, txFee, false);
                    cancelReplaceList(tx.getId(), ChainTxnReplaceAppType.WITHDRAW);
                } else {
                    // waiting for confirm
                }
            } else {
                // 提币失败时，状态改为 失败-待处理，需要运营介入，确认原tx失败无法replace时，做replay
                // 没有pending/ confirmed 的replace tx，如果该tx 存在replace tx，则tx的状态会在replace logic里处理
                if (latestChainBlock.getBlockNumber() - result.getBlockNumber() > chainService.getConfirmBlocks(chain) &&
                        chainTxnReplaceMapper.selectByAppIdAndAppType(tx.getId(), ChainTxnReplaceAppType.WITHDRAW).stream()
                                .noneMatch(e -> ChainCommonStatus.TRANSACTION_ON_CHAIN.equals(e.getStatus()) &&
                                        ChainCommonStatus.TRANSACTION_PENDING_CONFIRMED.equals(e.getStatus()) &&
                                        ChainCommonStatus.TRANSACTION_CONFIRMED.equals(e.getStatus()))
                ) {
                    tx.setUpdateTime(System.currentTimeMillis());
                    tx.setVersion(tx.getVersion() + 1);
                    tx.setStatus(WithdrawStatus.TRANSACTION_FAILED_PENDING_CHECK.getCode());
                    // 更新状态为失败
                    log.info("scanWithdrawOne update={}", tx);
                    chainDepositWithdrawHisMapper.updateById(tx);
                    //原tx已经失败，所以replace也会失败
                    cancelReplaceList(tx.getId(), ChainTxnReplaceAppType.WITHDRAW);
                    // 发送通知给运维介入
//                    notificationService.sendNotificationAsync(NotificationDto.builder()
//                            .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                            .values(ImmutableMap.of(
//                                    "ts", System.currentTimeMillis(),
//                                    "module", "withdraw",
//                                    "content", tx.getId() + " failed"))
//                            .build());
                } else {
                    // waiting for confirm
                }
            }
        }
//        else {
//            // tx 未打包，查找是否有对应的replace tx his
//            List<ChainTxnReplace> replaceList = chainTxnReplaceMapper.selectByAppIdAndAppType(tx.getId(), ChainTxnReplaceAppType.WITHDRAW);
//            if (replaceList.isEmpty()) {
//                // 不存在replace tx, 返回，下次继续扫描
//                return;
//            }
//            if (replaceList.stream().anyMatch(e -> ChainCommonStatus.TRANSACTION_CONFIRMED.equals(e.getStatus()))) {
//                // 存在被链上安全确认的replace tx
//                updateWithdrawTxn(tx, 0L, "", BigDecimal.ZERO, true);
//            }
//        }
    }

    private void cancelReplaceList(Long appId, ChainTxnReplaceAppType appType) {
        // 确认replace tx标为失败
        List<ChainTxnReplace> chainTxnReplaceList = chainTxnReplaceMapper.selectByAppIdAndAppType(appId, appType);
        chainTxnReplaceList.forEach(e -> {
            e.setStatus(ChainCommonStatus.TRANSACTION_CANCELLED);
            chainTxnReplaceMapper.updateByPrimaryKey(e);
        });
    }
    
    private void updateWithdrawTxn(ChainDepositWithdrawHis tx, ChainTxnReplace replaceTx, long blockNumber, String blockHash, BigDecimal txFee, boolean isReplace) {
        tx.setUpdateTime(System.currentTimeMillis());
        tx.setVersion(tx.getVersion() + 1);
        tx.setBlockNumber(blockNumber);
        tx.setBlockHash(blockHash);
        tx.setTxFee(txFee);
        tx.setIsReplace(isReplace);
        tx.setStatus(WithdrawStatus.TRANSACTION_CONFIRMED.getCode());
        // 更新状态
        chainDepositWithdrawHisMapper.updateById(tx);
        // 更新财务记录为成功
        userAccountActionService.updateStatusByActionUserIdSource(AccountAction.WITHDRAW, tx.getUserId(), String.valueOf(tx.getId()), CommonActionStatus.SUCCESS);
        log.info("updateWithdrawTxn update={}, replaceTx={}", tx, replaceTx);

        List<UserAccount> accounts = walletAccountService.getAccounts(tx.getUserId());
        // 通知账户变更
        kafkaProducer.send(KafkaTopic.TOPIC_ACCOUNT_UPDATE, JSONUtil.toJsonStr(NotificationReq.builder()
                .notificationType(NoticeTypeEnum.ACCOUNT_BALANCE_CHANGE.getCode())
                .userSource(TargetSource.UC.name())
                .ucUserIds(ImmutableList.of(tx.getUserId()))
                .values(ImmutableMap.of(
                        "ts", System.currentTimeMillis(),
                        "currency", tx.getCurrency(),
                        "change", tx.getAmount(),
                        "after", CollectionUtils.isEmpty(accounts) ? "" : accounts.get(0).getAvailable()))
                .build()));
        log.info("send balance change notification userid:{}, ts:{},currency:{},change:{}", tx.getUserId(), System.currentTimeMillis(), tx.getCurrency(), tx.getAmount());


        // 发送通知用户提示提币成功
        kafkaProducer.sendAsync(KafkaTopic.TOPIC_ACCOUNT_UPDATE,JSONUtil.toJsonStr(NotificationReq.builder()
                .notificationType(NoticeTypeEnum.ACCOUNT_WITHDRAW.getCode())
                .userSource(TargetSource.UC.name())
                .ucUserIds(ImmutableList.of(tx.getUserId()))
                .values(ImmutableMap.of(
                        "ts", System.currentTimeMillis(),
                        "currency", tx.getCurrency(),
                        "amount", tx.getAmount()))
                .build()));
        log.info("send withdraw notification ts:{},currency:{},amount:{}", System.currentTimeMillis(), tx.getCurrency(), tx.getAmount());

    }

    /**
     * defi提币的处理：标记成功或者返回资产
     *
     * @param chain      defi-chain
     * @param chainBlock
     * @param tx
     * @throws Exception
     */
    private void scanWithdrawOneOnChain(Chain chain, ChainBlock chainBlock, ChainDepositWithdrawHis tx) throws Exception {
        long id = tx.getId();
        ChainDepositWithdrawSigHis sigHistory = chainDepositWithdrawHisService.getSigHistory(id, tx.getUserId());
        if (sigHistory == null) {
            log.warn("scanWithdrawOneOnChain failed to find sigHis for transaction={}", tx);
            return;
        }
        // 截止日期(秒)
        long deadline = sigHistory.getDeadline();
        // 截止日期增加5分钟Buffer
        long deadlineBuffer = Long.parseLong(systemConfigService.getValue(AccountSystemConfig.CHAIN_DEFI_WITHDRAW_DEADLINE, "300"));
        Chain relayOn = chain.getRelayOn();
        String defiDepositWithdrawContract = systemWalletAddressService.getOne(relayOn, WalletAddressType.DEFI_DEPOSIT_WITHDRAW_CONTRACT);
        long claimedBlockNumber = chainService.getContractClaimHistory(relayOn, defiDepositWithdrawContract, id);
        int confirmBlocks = chainService.getConfirmBlocks(relayOn);

        if (claimedBlockNumber > 0 && chainBlock.getBlockNumber() - claimedBlockNumber >= confirmBlocks) {
            // 提币确认, 更新提币状态, 减除冻结资产
            boolean result = walletAccountService.updateFreeze(tx.getUserId(), tx.getCurrency(), tx.getAmount().negate(), true);
            Utils.check(result, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);

            tx.setUpdateTime(System.currentTimeMillis());
            tx.setVersion(tx.getVersion() + 1);
            tx.setStatus(WithdrawStatus.TRANSACTION_CONFIRMED.getCode());
            // 更新提币状态
            log.info("scanWithdrawOneOnChain update={}", tx);
            chainDepositWithdrawHisMapper.updateById(tx);
            // 更新财务记录状态
            userAccountActionService.updateStatusByActionUserIdSource(AccountAction.WITHDRAW, tx.getUserId(), String.valueOf(tx.getId()), CommonActionStatus.SUCCESS);
        } else if (claimedBlockNumber == 0 && chainBlock.getBlockTime() > (deadline + deadlineBuffer)
                && (System.currentTimeMillis() > chainBlock.getBlockTime() * 1000)) {
            // 提币超时, 更新提币状态, 解冻冻结资产 (上面判断当前时间是为了防止block的时间越界)
            boolean result = walletAccountService.unfreeze(tx.getUserId(), tx.getCurrency(), tx.getAmount());
            Utils.check(result, ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);

            tx.setUpdateTime(System.currentTimeMillis());
            tx.setVersion(tx.getVersion() + 1);
            tx.setStatus(WithdrawStatus.TRANSACTION_CANCELLED.getCode());
            // 更新状态
            log.info("scanWithdrawOneOnChain update={}", tx);
            chainDepositWithdrawHisMapper.updateById(tx);
            // 更新财务记录状态
            userAccountActionService.updateStatusByActionUserIdSource(AccountAction.WITHDRAW, tx.getUserId(), String.valueOf(tx.getId()), CommonActionStatus.FAILED);
        }
    }

    private void processUnSafeConfirmedExchangeHis(ChainExchangeHis tx, ChainTransactionReceipt result) {
        if (tx.getStatus().equals(ChainCommonStatus.TRANSACTION_ON_CHAIN)) { // 不需要判断
            String uniswapEventTopic = systemConfigService.getValue(AccountSystemConfig.CHAIN_UNISWAP_EVENT_TOPIC);
            // 第一次拿到 txn reciept，解读 receipt，获取 uniswap amountOut， 并更新 his 状态
            // @dev 注意调用 uniswap router v2 执行兑换时，path 中可能有多于两个币种的地址。因此log事件中，只取最后一个swap事件，来解析最后我们换得的币的数量。
            com.seeds.account.chain.dto.Log lastSwapLog = null;
            for (int logi = 0; logi < result.getLogs().size(); logi++) {
                com.seeds.account.chain.dto.Log l = result.getLogs().get(logi);
                if (uniswapEventTopic.equalsIgnoreCase(l.getTopics().get(0))) {
                    lastSwapLog = l;
                }
            }
            if (lastSwapLog == null) {
                // 没有找到 swap event，不可能发生，需要人工介入的异常情况
                chainExchangeHisMapper.updateStatusByPrimaryKey(tx.getId(), ChainCommonStatus.TRANSACTION_FAILED);
//                notificationService.sendNotificationAsync(NotificationDto.builder()
//                        .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                        .values(ImmutableMap.of(
//                                "ts", System.currentTimeMillis(),
//                                "module", "exchange",
//                                "content", tx.getId() + " failed to get swap event"))
//                        .build());
                return;
            }
            // 解码 event
            EventValues eventValues = staticExtractEventParameters(UNISWAP_SWAPEVENT, lastSwapLog);

            // 核对 to 地址
            String toAddr = ((Address) eventValues.getIndexedValues().get(1)).getValue();
            if (!tx.getToAddress().equalsIgnoreCase(toAddr)) {
                // to 地址与 his 记录不符，不可能发生，需要人工介入的异常情况
                chainExchangeHisMapper.updateStatusByPrimaryKey(tx.getId(), ChainCommonStatus.TRANSACTION_FAILED);
//                notificationService.sendNotificationAsync(NotificationDto.builder()
//                        .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                        .values(ImmutableMap.of(
//                                "ts", System.currentTimeMillis(),
//                                "module", "exchange",
//                                "content", tx.getId() + " to address not match"))
//                        .build());
                return;
            }
            // 获取 amountOut
            BigInteger amount0 = ((Uint256) eventValues.getNonIndexedValues().get(2)).getValue();
            BigInteger amount1 = ((Uint256) eventValues.getNonIndexedValues().get(3)).getValue();
            BigInteger amountOut = amount0.add(amount1);

            // 折算精度
            ChainContractDto toCurrencyConfig = chainContractService.get(tx.getChain().getCode(), tx.getTargetCurrency());
            BigDecimal unscaledAmountOut = new BigDecimal(amountOut).divide(BigDecimal.TEN.pow(toCurrencyConfig.getDecimals()));

            log.info("processExchangeHis - got swap result, txhash: {}, amountOut: {}", tx.getTxHash(), unscaledAmountOut);

            tx.setTargetAmount(unscaledAmountOut);
        }
    }

    private EventValues staticExtractEventParameters(Event event, com.seeds.account.chain.dto.Log log) {
        List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (topics != null && topics.size() != 0 && ((String) topics.get(0)).equals(encodedEventSignature)) {
            List<Type> indexedValues = new ArrayList();
            List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
            List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();

            for (int i = 0; i < indexedParameters.size(); ++i) {
                Type value = FunctionReturnDecoder.decodeIndexedValue((String) topics.get(i + 1), (TypeReference) indexedParameters.get(i));
                indexedValues.add(value);
            }

            return new EventValues(indexedValues, nonIndexedValues);
        } else {
            return null;
        }
    }


    @Override
    public void processCollectSuccess(AddressCollectHis tx, long blockNumber, String blockHash, BigDecimal txFee, boolean isReplace) {
        // 被链上安全确认的replace tx
        tx.setUpdateTime(System.currentTimeMillis());
        tx.setVersion(tx.getVersion() + 1);
        tx.setBlockNumber(blockNumber);
        tx.setBlockHash(blockHash);
        tx.setTxFee(txFee);
        tx.setStatus(isReplace ? ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED.getCode() : ChainCommonStatus.TRANSACTION_CONFIRMED.getCode());
        log.info("processCollectSuccess update={}", tx);
        addressCollectHisMapper.updateByPrimaryKey(tx);
    }

    private void processExchangeSuccess(ChainExchangeHis tx, long blockNumber, String blockHash, BigDecimal txFee, long gasUsed, boolean isReplace) {
        tx.setUpdateTime(System.currentTimeMillis());
        tx.setVersion(tx.getVersion() + 1);
        tx.setBlockNumber(blockNumber);
        tx.setBlockHash(blockHash);
        tx.setTxFee(txFee);
        tx.setGasUsed(gasUsed);
        tx.setStatus(isReplace ? ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED : ChainCommonStatus.TRANSACTION_CONFIRMED);
        // 更新状态
        chainExchangeHisMapper.updateByPrimaryKey(tx);

        // 更新 fromCurrency 冻结, toCurrency available
        boolean isSuccess = walletAccountService.updateFreezeAndAvailable(
                tx.getUserId(),
                tx.getSourceCurrency(),
                tx.getSourceAmount().negate(),
                tx.getTargetCurrency(),
                tx.getTargetAmount()
        );

        if (!isSuccess) {
            // 发送通知给运维介入
//            notificationService.sendNotificationAsync(NotificationDto.builder()
//                    .notificationType(OpsAction.OPS_TECH.getNotificationType())
//                    .values(ImmutableMap.of(
//                            "ts", System.currentTimeMillis(),
//                            "module", "exchange",
//                            "content", tx.getId() + " failed"))
//                    .build());
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR,
                    String.format("exchange, failed to update fromAccount freeze/ toAccount available, userId=%s, fromCurrency=%s, fromAmount=%s, toCurrency=%s, toAmount=%s",
                            tx.getUserId(), tx.getSourceCurrency(), tx.getSourceAmount(), tx.getTargetCurrency(), tx.getTargetAmount()));
        }
        // 记录执行历史
        userAccountActionService.createHistory(tx.getUserId(), tx.getSourceCurrency(),
                AccountAction.CHAIN_EXCHANGE_OUT, tx.getSourceAmount());
        userAccountActionService.createHistory(tx.getUserId(), tx.getTargetCurrency(),
                AccountAction.CHAIN_EXCHANGE_IN, tx.getTargetAmount());

        // 发送通知mgt用户兑换成功
//        notificationService.sendNotificationAsync(NotificationDto.builder()
//                .notificationType(OpsAction.OPS_SUPPORT.getNotificationType())
//                .values(ImmutableMap.of(
//                        "ts", System.currentTimeMillis(),
//                        "module", "exchange",
//                        "content", tx.getId() + " succeeded"))
//                .build());
    }

    private Long execute(ChainDepositWithdrawHis tx, ChainTxnData chainTxnData) throws Exception {
        // 只有pending超过阈值或者 失败-待处理 的tx 才能进行replay
        RawTransactionDto rawTransactionDto = WithdrawStatus.TRANSACTION_FAILED_PENDING_CHECK.getCode().equals(tx.getStatus()) ? replayTransaction(tx) :
                replayTransaction(tx.getChain(), tx.getFromAddress(), tx.getTxToAddress(), tx.getTxHash(), new BigInteger(tx.getTxValue()),
                        new BigInteger(tx.getNonce()), BigInteger.valueOf(tx.getGasPrice()), BigInteger.valueOf(tx.getGasLimit()), chainTxnData.getData());

        tx.setUpdateTime(System.currentTimeMillis());
        tx.setVersion(tx.getVersion() + 1);
        tx.setTxToAddress(rawTransactionDto.getTo());
        tx.setTxHash(rawTransactionDto.getTxnHash());
        tx.setTxValue(rawTransactionDto.getValue().toString());
        tx.setNonce(rawTransactionDto.getNonce().toString());
        tx.setGasPrice(rawTransactionDto.getGasPrice().longValue());
        tx.setGasLimit(rawTransactionDto.getGasLimit().longValue());
        tx.setStatus(WithdrawStatus.TRANSACTION_ON_CHAIN.getCode());

        chainDepositWithdrawHisService.updateById(tx);
        // add new chain transaction data his
        if (chainTxnData != null) {
            chainTxnData.setData(rawTransactionDto.getData());
            chainTxnData.setUpdateTime(System.currentTimeMillis());
            chainTxnDataMapper.updateByPrimaryKey(chainTxnData);
            log.info("update chainTxnData={}", chainTxnData);
        } else {
            chainTxnData = ChainTxnData.builder()
                    .version(AccountConstants.DEFAULT_VERSION)
                    .appId(tx.getId())
                    .appType(ChainTxnDataAppType.WITHDRAW)
                    .data(rawTransactionDto.getData())
                    .createTime(System.currentTimeMillis())
                    .updateTime(System.currentTimeMillis())
                    .build();
            chainTxnDataMapper.insert(chainTxnData);
            log.info("insert new chainTxnData={}", chainTxnData);
        }
        return tx.getId();
    }

    private Long execute(AddressCollectHis tx, String data) throws Exception {
        RawTransactionDto rawTransactionDto = replayTransaction(tx.getChain(), tx.getFromAddress(), tx.getTxToAddress(), tx.getTxHash(),
                new BigInteger(tx.getTxValue()), new BigInteger(tx.getNonce()), BigInteger.valueOf(tx.getGasPrice()), BigInteger.valueOf(tx.getGasLimit()), data);
        // 更新原表
        tx.setTxHash(rawTransactionDto.getTxnHash());
        tx.setNonce(rawTransactionDto.getNonce().toString());
        tx.setStatus(ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode());
        tx.setVersion(tx.getVersion() + 1);
        tx.setUpdateTime(System.currentTimeMillis());

        addressCollectHisMapper.updateByPrimaryKey(tx);
        return tx.getId();
    }

    private Long execute(Chain chain, Long appId, ChainTxnReplaceAppType appType, String txHash, String txValue, String fromAddress, String toAddress,
                         String nonce, BigInteger gasPrice, BigInteger gasLimit, String data) throws Exception {
        // 过滤掉DEFI提币
        if (!Chain.SUPPORT_LIST.contains(chain)) {
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "chain not in support list");
        }
        try {
            RawTransactionDto rawTransactionDto = chainService.replaceTransaction(chain,
                    RawTransactionDto.builder()
                            .value(new BigInteger(txValue))
                            .to(toAddress)
                            .nonce(new BigInteger(nonce))
                            .gasPrice(gasPrice)
                            .gasLimit(gasLimit)
                            .data(data)
                            .build(),
                    fromAddress
            );
            // 3.插入 chain ChainTxnReplace his
            ChainTxnReplace replacement = ChainTxnReplace.builder()
                    .version(AccountConstants.DEFAULT_VERSION)
                    .appType(appType)
                    .appId(appId)
                    .chain(chain)
                    .gasPrice(gasPrice.longValue())
                    .gasLimit(gasLimit.longValue())
                    .gasUsed(0L)
                    .txFee(BigDecimal.ZERO)
                    .txHash(rawTransactionDto.getTxnHash())
                    .txValue(rawTransactionDto.getValue() != null ? rawTransactionDto.getValue().toString() : "0")
                    .blockNumber(0L)
                    .blockHash("")
                    .nonce(rawTransactionDto.getNonce().toString())
                    .status(ChainCommonStatus.TRANSACTION_ON_CHAIN)
                    .createTime(System.currentTimeMillis())
                    .updateTime(System.currentTimeMillis())
                    .build();
            chainTxnReplaceMapper.insert(replacement);
            log.info("insert new replacement transaction={}", replacement);
            // add new chain transaction data his
            ChainTxnData transactionData = ChainTxnData.builder()
                    .version(AccountConstants.DEFAULT_VERSION)
                    .appId(replacement.getId())
                    .appType(ChainTxnDataAppType.TXN_REPLACEMENT)
                    .data(rawTransactionDto.getData())
                    .createTime(System.currentTimeMillis())
                    .updateTime(System.currentTimeMillis())
                    .build();
            chainTxnDataMapper.insert(transactionData);
            log.info("insert new transaction data={}", transactionData);
            return replacement.getId();
        } catch (Exception e) {
            log.error("error when processing chain replaceTransaction, ", e);
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, e.getMessage());
        }
    }

    private RawTransactionDto replayTransaction(ChainDepositWithdrawHis tx) throws Exception {
        // 过滤掉DEFI提币
        if (!Chain.SUPPORT_LIST.contains(tx.getChain())) {
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "chain not in support list");
        }
        if (!WithdrawStatus.TRANSACTION_FAILED_PENDING_CHECK.getCode().equals(tx.getStatus())) {
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "not in support status");
        }
        // 提币 失败-待处理 case
        // 1. 链上打包后执行失败，并且经过安全确认
        // 2. 发送到链上时出现exception 导致未上链
        // 此时不需要判断tx 直接重新发起链上提币
        if (Chain.SUPPORT_NONCE_LIST.contains(tx.getChain()) && tx.getNonce() != null) {
            passSafeNonceCheck(tx.getChain(), tx.getFromAddress(), new BigInteger(tx.getNonce()));
        }

        RawTransactionDto rawTransactionDto;
        try {
            BigDecimal amountToChain = tx.getAmount().subtract(tx.getFeeAmount());
            rawTransactionDto = chainService.internalSendTransaction(tx.getChain(), tx.getCurrency(), tx.getFromAddress(), tx.getToAddress(), amountToChain, chainService.getGasPrice(tx.getChain()), chainService.getGasLimit(tx.getChain()));
        } catch (Exception e) {
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, e.getMessage());
        }

        return rawTransactionDto;
    }

    private RawTransactionDto replayTransaction(Chain chain, String fromAddress, String toAddress, String txHash, BigInteger txValue,
                                                BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String data) throws Exception {
        return replayTransaction(chain, fromAddress, toAddress, txHash, txValue, nonce, gasPrice, gasLimit, data, false);
    }

    private RawTransactionDto replayTransaction(Chain chain, String fromAddress, String toAddress, String txHash, BigInteger txValue,
                                                BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String data, boolean isForce) throws Exception {
        // 过滤掉DEFI提币
        if (!Chain.SUPPORT_LIST.contains(chain)) {
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "chain not in support list");
        }
        RawTransactionDto rawTransactionDto;
        // 正常情况下的replay
        // 当前tx 是否存在
        ChainTransaction ethTransaction = chainService.getTransactionByTxHash(chain, txHash);

        if (!isForce && ethTransaction != null) {
            // 原 transaction 存在，对提币失败/mcd链上失败 case，我们允许进行replay, 其他情况下不允许replay
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "original tx is still pending on chain, replay is not allowed");
        }
        if (Chain.SUPPORT_NONCE_LIST.contains(chain)) {
            passSafeNonceCheck(chain, fromAddress, nonce);
        }
        // 发起replay
        BigInteger pendingNonce = chainService.getPendingNonce(chain, fromAddress);
        try {
            rawTransactionDto = chainService.replaceTransaction(chain,
                    RawTransactionDto.builder()
                            .value(txValue)
                            .to(toAddress)
                            .nonce(pendingNonce)
                            .gasPrice(gasPrice)
                            .gasLimit(gasLimit)
                            .data(data)
                            .build(),
                    fromAddress
            );
        } catch (Exception e) {
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, e.getMessage());
        }

        return rawTransactionDto;
    }

    private void passSafeNonceCheck(Chain chain, String fromAddress, BigInteger nonce) throws IOException {
        BigInteger confirmedSafeNonce = chainService.getSafeConfirmedNonce(chain, fromAddress);
        if (confirmedSafeNonce.compareTo(BigInteger.ZERO) <= 0 || confirmedSafeNonce.compareTo(nonce) < 0) {
            // fromAddress nonce 未被安全确认
            log.error("original tx nonce:{} is greater than safe confirm nonce:{}", nonce, confirmedSafeNonce);
            throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "original tx nonce is greater than safe confirm nonce");
        }
    }

    private Map<Chain, Long> getChainExpireTime() {
        String config = systemConfigService.getValue(AccountSystemConfig.CHAIN_TXN_EXPIRE_TIME);
        List<ChainExpireConfigDto> expireConfigList = (config != null && config.length() > 0)
                ? JsonUtils.readValue(config, new com.fasterxml.jackson.core.type.TypeReference<List<ChainExpireConfigDto>>() {
        })
                : Lists.newArrayList();
        return expireConfigList.stream().collect(Collectors.toMap(e -> Chain.fromCode(e.getChain()), ChainExpireConfigDto::getExpireTimeInSec));
    }

    private BigInteger getChainGasPrice(Chain chain) {
        return BigInteger.valueOf(chainService.getGasPrice(chain));
    }

    private ChainTxnDto convert2Dto(ChainDepositWithdrawHis e, BigInteger chainGasPrice, int type) {
        ChainTxnDto obj = ObjectUtils.copy(e, new ChainTxnDto());
        obj.setGasPrice(BigInteger.valueOf(e.getGasPrice()));
        obj.setGasLimit(BigInteger.valueOf(e.getGasLimit()));
        obj.setType(type);
        obj.setNonce(new BigInteger(e.getNonce()));
        obj.setChainGasPrice(chainGasPrice);
        obj.setChain(e.getChain().getCode());
        // 内部提币不上链，非内部提币才能查到
        if (e.getInternal() != 1) {
            try {
                obj.setConfirmedSafeNonce(chainService.getSafeConfirmedNonce(e.getChain(), e.getFromAddress()));
            } catch (IOException ioException) {
                log.error("error, ", ioException);
            }
        }
        return obj;
    }

    private ChainTxnDto convert2Dto(AddressCollectHis e, BigInteger chainGasPrice, int type) {
        ChainTxnDto obj = ObjectUtils.copy(e, new ChainTxnDto());
        obj.setType(type);
        obj.setNonce(new BigInteger(e.getNonce()));
        obj.setChainGasPrice(chainGasPrice);
        obj.setGasPrice(BigInteger.valueOf(e.getGasPrice()));
        obj.setGasLimit(BigInteger.valueOf(e.getGasLimit()));
        obj.setChain(e.getChain().getCode());
        try {
            obj.setConfirmedSafeNonce(chainService.getSafeConfirmedNonce(e.getChain(), e.getFromAddress()));
        } catch (IOException ioException) {
            log.error("error, ", ioException);
        }
        return obj;
    }

    private ChainTxnDto convert2Dto(ChainTxnReplace e) {
        ChainTxnDto obj = ObjectUtils.copy(e, new ChainTxnDto());
        obj.setGasPrice(BigInteger.valueOf(e.getGasPrice()));
        obj.setGasLimit(BigInteger.valueOf(e.getGasLimit()));
        obj.setType(e.getAppType().getCode());
        obj.setStatus(e.getStatus().getCode());
        obj.setChain(e.getChain().getCode());
        obj.setNonce(new BigInteger(e.getNonce()));
        return obj;
    }


    @Override
    public void getAndMetricCurrentGasPriceOracle() {
        for (Chain chain : Chain.SUPPORT_GAS_PRICE_LIST) {
            try {
                chainService.getAndMetricCurrentGasPriceOracle(chain);
            } catch (Exception e) {
                log.error("getAndMetricCurrentGasPriceOracle chain={}", chain, e);
            }
        }
    }

    @Override
    public SystemWalletAddressDto createSystemWalletAddress(Chain chain) throws Exception {
        Utils.check(Chain.SUPPORT_CREATE_ADDRESS_LIST.contains(chain), ErrorCode.ACCOUNT_INVALID_CHAIN);

        List<String> addresses = chainService.createAddresses(chain, 1);
        Assert.isTrue(addresses.size() == 1, "failed to create new address");
        SystemWalletAddressDto systemWalletAddressDto = SystemWalletAddressDto.builder()
                .type(WalletAddressType.HOT.getCode())
                .chain(chain.getCode())
                .address(addresses.get(0))
                .comments("")
                .tag("hot")
                .status(CommonStatus.ENABLED.getCode())
                .build();
        log.info("createWalletAddress systemWalletAddressDto={}", systemWalletAddressDto);
        systemWalletAddressService.add(systemWalletAddressDto);
        return systemWalletAddressDto;
    }
}
