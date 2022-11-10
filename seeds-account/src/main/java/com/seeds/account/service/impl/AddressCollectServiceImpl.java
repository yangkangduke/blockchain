package com.seeds.account.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.seeds.account.AccountConstants;
import com.seeds.account.anno.SingletonLock;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.*;
import com.seeds.account.enums.*;
import com.seeds.account.ex.AccountException;
import com.seeds.account.ex.ActionDeniedException;
import com.seeds.account.mapper.*;
import com.seeds.account.model.*;
import com.seeds.account.service.*;
import com.seeds.account.util.AddressUtils;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.common.redis.account.RedisKeys;
import com.seeds.uc.feign.UserCenterFeignClient;
import com.seeds.wallet.dto.RawTransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressCollectServiceImpl implements IAddressCollectService {

    @Resource
    ChainTxnReplaceMapper chainTxnReplaceMapper;
    @Autowired
    IChainActionService chainActionService;
    @Autowired
    AddressCollectHisMapper addressCollectHisMapper;
    @Autowired
    IChainService chainService;
    @Autowired
    AddressCollectOrderHisMapper addressCollectOrderHisMapper;
    @Autowired
    ISystemConfigService systemConfigService;
    @Autowired
    IChainContractService chainContractService;
    @Autowired
    IChainDepositWithdrawHisService chainDepositWithdrawHisService;
    @Resource
    ChainDepositAddressMapper chainDepositAddressMapper;
    @Autowired
    ISystemWalletAddressService systemWalletAddressService;
    @Autowired
    IAddressCollectHisService addressCollectHisService;
    @Resource
    ChainTxnDataMapper transactionDataMapper;
    @Autowired
    IAsyncService asyncService;
    @Autowired
    RedissonClient client;
    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    @Override
    @SingletonLock(key = "/seeds/account/chain/collect")
    public void scanCollect() throws Exception {
        // 先去处理每笔PROCESSING的交易
        checkAndUpdateCollectStatus();

        // 再去处理归集订单的状态
        checkAndUpdateOrderStatus();
    }

    private void checkAndUpdateCollectStatus() throws Exception {
        // 先处理replace
        chainActionService.scanTxnReplace(ChainTxnReplaceAppType.HOT_WALLET_TRANSFER);
        chainActionService.scanTxnReplace(ChainTxnReplaceAppType.CASH_COLLECT);
        chainActionService.scanTxnReplace(ChainTxnReplaceAppType.GAS_TRANSFER);

        // 获取处理中的列表
        List<AddressCollectHis> list = addressCollectHisMapper.getByStatus(ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode());
        Map<Chain, ChainBlock> latestBlockMap = chainActionService.getLatestBlock(list.stream().distinct().map(AddressCollectHis::getChain).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latestBlockMap)) {
            return;
        }
        log.info("checkAndUpdateCollectStatus list size={}", list.size());
        for (AddressCollectHis tx : list) {
            try {
                ChainTransactionReceipt result = chainService.getTransactionReceiptByTxHash(tx.getChain(), tx.getTxHash());
                log.info("checkAndUpdateCollectStatus chain={} txHash={} result={}", tx.getChain(), tx.getTxHash(), result);
                if (result != null) {
                    if (result.getStatus()) {
                        long blockNumber = result.getBlockNumber();
                        if (latestBlockMap.containsKey(tx.getChain()) &&
                                latestBlockMap.get(tx.getChain()).getBlockNumber() - blockNumber > chainService.getConfirmBlocks(tx.getChain())) {
                            String blockHash = result.getBlockHash();
                            long gasUsed = result.getGasUsed();
                            long gasPrice = tx.getGasPrice();
                            // 更新状态为成功，并计算gasFee
                            BigDecimal txFee = chainService.calcTxFee(tx.getChain(), gasUsed, gasPrice);

                            chainActionService.processCollectSuccess(tx, blockNumber, blockHash, txFee, false);
                            cancelReplaceList(tx);
                        }
                    } else {
                        if (latestBlockMap.containsKey(tx.getChain()) &&
                                latestBlockMap.get(tx.getChain()).getBlockNumber() - result.getBlockNumber() > chainService.getConfirmBlocks(tx.getChain()) &&
                                chainTxnReplaceMapper.selectByAppIdAndAppType(tx.getId(), getReplaceAppType(tx)).stream()
                                        .noneMatch(e -> ChainCommonStatus.TRANSACTION_ON_CHAIN.equals(e.getStatus()) &&
                                                ChainCommonStatus.TRANSACTION_PENDING_CONFIRMED.equals(e.getStatus()) &&
                                                ChainCommonStatus.TRANSACTION_CONFIRMED.equals(e.getStatus()))
                        ) {
                            // 更新状态失败
                            tx.setUpdateTime(System.currentTimeMillis());
                            tx.setVersion(tx.getVersion() + 1);
                            tx.setStatus(ChainCommonStatus.TRANSACTION_FAILED.getCode());
                            log.info("checkAndUpdateCollectStatus update to fail={}", tx);
                            addressCollectHisMapper.updateByPrimaryKey(tx);
                            cancelReplaceList(tx);
                        } else {
                            // wait to confirm
                        }
                    }
                }
//                else {
//                    List<ChainTxnReplace> replaceList = chainTxnReplaceMapper.selectByAppIdAndAppType(tx.getId(), getReplaceAppType(tx));
//                    if (replaceList.isEmpty()) {
//                        // 不存在replace tx, 返回，下次继续扫描
//                        log.info("checkAndUpdateCollectStatus no replace for {}, returning...", tx.getId());
//                        continue;
//                    }
//                    if (replaceList.stream().anyMatch(e -> ChainCommonStatus.TRANSACTION_CONFIRMED.equals(e.getStatus()))) {
//                        processCollectSuccess(tx, 0L, "", BigDecimal.ZERO, true);
//                    }
//                }
            } catch (Exception e) {
                log.error("failed to getTransactionReceiptByTxHash={}", tx.getTxHash(), e);
            }
        }
    }

    private ChainTxnReplaceAppType getReplaceAppType(AddressCollectHis tx) {
        ChainTxnReplaceAppType appType;
        if (tx.getOrderId() == 0) {
            // 热钱包划转
            appType = ChainTxnReplaceAppType.HOT_WALLET_TRANSFER;
        } else {
            // 查看原始order type
            AddressCollectOrderHis orderHis = addressCollectOrderHisMapper.selectByPrimaryKey(tx.getOrderId());
            if (orderHis == null) {
                throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "failed to find the collect order his");
            }
            if (FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode().equals(orderHis.getType())) {
                // 钱包归集
                appType = ChainTxnReplaceAppType.CASH_COLLECT;
            } else if (FundCollectOrderType.FROM_SYSTEM_TO_USER.getCode().equals(orderHis.getType())) {
                // Gas 划转
                appType = ChainTxnReplaceAppType.GAS_TRANSFER;
            } else {
                throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "invalid collect order type");
            }
        }
        return appType;
    }

    private void cancelReplaceList(AddressCollectHis tx) {
        // 确认replace tx标为失败
        List<ChainTxnReplace> chainTxnReplaceList = chainTxnReplaceMapper.selectByAppIdAndAppType(tx.getId(), getReplaceAppType(tx));
        chainTxnReplaceList.forEach(e -> {
            e.setStatus(ChainCommonStatus.TRANSACTION_CANCELLED);
            chainTxnReplaceMapper.updateByPrimaryKey(e);
        });
    }

    private void checkAndUpdateOrderStatus() {
        // 获取处理中的订单
        List<AddressCollectOrderHis> list = addressCollectOrderHisMapper.getByStatus(FundCollectOrderStatus.PROCESSING.getCode());
        if (list.size() == 0) {
            return;
        }
        list.forEach(collectOrder -> {
            long orderId = collectOrder.getId();
            // 读取订单的列表
            List<AddressCollectHis> collectHisList = addressCollectHisMapper.getByOrderId(orderId);
            // 如果list 为空，则不处理order
            if (collectHisList.isEmpty()) {
                log.warn("empty collect his with orderId={}", orderId);
                return;
            }

            // 原始交易可能会有多笔替换交易，在最后一笔替换交易成功后，需要更新collectOrder的状态。

            List<String> confirmedReplaceNonce = chainTxnReplaceMapper.getListByChainStatusAndType(collectOrder.getChain(),
                    ChainCommonStatus.TRANSACTION_CONFIRMED,
                    FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode().equals(collectOrder.getType()) ? ChainTxnReplaceAppType.CASH_COLLECT : ChainTxnReplaceAppType.GAS_TRANSFER).stream().map(p -> p.getNonce()).collect(Collectors.toList());


            List<String> pendingReplaceNonce = chainTxnReplaceMapper.getListByChainStatusAndType(collectOrder.getChain(),
                    ChainCommonStatus.TRANSACTION_ON_CHAIN,
                    FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode().equals(collectOrder.getType()) ? ChainTxnReplaceAppType.CASH_COLLECT : ChainTxnReplaceAppType.GAS_TRANSFER).stream().map(p -> p.getNonce()).distinct().collect(Collectors.toList());

            // 查看是否所有的都已经处理完了
            if (collectHisList.stream().anyMatch(e -> e.getStatus() == ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode()) ||
                    // 存在replace中的,并且没有已经被链上确认的nonce
                    (!pendingReplaceNonce.isEmpty() && Collections.disjoint(confirmedReplaceNonce, pendingReplaceNonce))) {
                return;
            }
            // 统计所花费的txFee (不管成功失败，都统计)
            BigDecimal feeAmount = collectHisList.stream().map(AddressCollectHis::getTxFee).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 统计实际归集的数额 (只统计成功的)
            BigDecimal amount = collectHisList.stream()
                    .filter(e -> e.getStatus() == ChainCommonStatus.TRANSACTION_CONFIRMED.getCode() || e.getStatus() == ChainCommonStatus.TRANSACTION_CANCELLED_AND_REPLACED.getCode())
                    .map(AddressCollectHis::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            collectOrder.setUpdateTime(System.currentTimeMillis());
            collectOrder.setVersion(collectOrder.getVersion() + 1);
            collectOrder.setFeeAmount(feeAmount);
            collectOrder.setAmount(amount);
            collectOrder.setStatus(FundCollectOrderStatus.COMPLETED.getCode());
            log.info("checkAndUpdateOrderStatus collectOrder={}", collectOrder);
            addressCollectOrderHisMapper.updateByPrimaryKey(collectOrder);
        });
    }

    @Override
    @SingletonLock(key = "/kine/account/chain/scan-pending-collect-balances")
    public void scanPendingCollectBalances() {
        try {
            // disable
            // getPendingCollectBalances();
        } catch (Exception e) {
            log.error("scanPendingCollectBalances", e);
        }
    }


    @Override
    public Map<Chain, Map<String, BigDecimal>> getPendingCollectBalances() {
        Map<Chain, Map<String, BigDecimal>> chainAddressBalanceMap = new ConcurrentHashMap<>();
        Chain.SUPPORT_LIST.forEach(chain -> {
            List<AddressBalanceDto> list = this.getPendingCollectBalances(chain);

            Map<String, BigDecimal> total = Maps.newHashMap();
            list.forEach(e -> e.getBalances().forEach((currency, balance) -> {
                BigDecimal balanceTotal = total.computeIfAbsent(currency, k -> BigDecimal.ZERO);
                total.put(currency, balanceTotal.add(balance));
            }));

            chainAddressBalanceMap.put(chain, total);
        });
        return chainAddressBalanceMap;
    }

    private List<AddressBalanceDto> getPendingCollectBalances(Chain chain) {
        // 充币行为回查天数  原值默认为3
        int lookback = Integer.parseInt(systemConfigService.getValue(AccountSystemConfig.FUND_COLLECT_DEPOSIT_ADDRESS_LOOK_BACK, "100"));
        long startTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(lookback);
        long endTime = System.currentTimeMillis();

        List<ChainDepositAddressDto> addresses = chainDepositWithdrawHisService.getDepositAddress(chain, startTime, endTime);

        List<SystemWalletAddressDto> systemWalletAddress = systemWalletAddressService.getAll();
        List<String> userAddress = addresses.stream()
                .filter(p -> systemWalletAddress.stream().noneMatch(i -> Objects.equals(p.getAddress(), i.getAddress())))
                .map(ChainDepositAddressDto::getAddress)
                .collect(Collectors.toList());

        // batch操作中间的休眠天数
        long sleep = Long.parseLong(systemConfigService.getValue(AccountSystemConfig.FUND_COLLECT_REQUEST_BATCH_SLEEP, "500"));
        try {
            List<AddressBalanceDto> balancesOnBatch = chainService.getBalancesOnBatch(chain, userAddress, sleep);

            List<AddressBalanceDto> resultList = Lists.newArrayList();
            // rpc 获取用户的email
            GenericDto<Map<Long, String>> emailMap = userCenterFeignClient.getEmailByIds(addresses.stream().map(ChainDepositAddressDto::getUserId).collect(Collectors.toList()));

            if (null != emailMap && emailMap.getCode() == 200) {
                for (AddressBalanceDto balanceDto : balancesOnBatch) {
                    AddressBalanceDto dto = new AddressBalanceDto();
                    ObjectUtils.copy(balanceDto, dto);
                    for (ChainDepositAddressDto addressDto : addresses) {
                        if (addressDto.getAddress().equals(balanceDto.getAddress())) {
                            dto.setEmail(emailMap.getData().get(addressDto.getUserId()));
                        }
                    }
                    resultList.add(dto);
                }
            }
            return resultList;

        } catch (Exception e) {
            log.error("getPendingCollectBalances, ", e);
        }
        return Lists.newArrayList();
    }

    @Override
    public AddressCollectHisDto createFundCollect(FundCollectRequestDto requestDto) {
        log.info("createFundCollect requestDto={}", requestDto);
        Chain chain = Chain.fromCode(requestDto.getChain());
        Utils.check(Chain.SUPPORT_LIST.contains(chain), ErrorCode.ACCOUNT_INVALID_CHAIN);

        String currency = requestDto.getCurrency();
        String toAddress = requestDto.getToAddress();

        // 不支持提币到合约地址
        if (!Objects.equals(currency, chain.getNativeToken())) {
            ChainContractDto chainContractDto = chainContractService.get(requestDto.getChain(), currency);
            if (chainContractDto == null) {
                throw new ActionDeniedException("no contract for " + currency);
            }
            if (ObjectUtils.isAddressEquals(toAddress, chainContractDto.getAddress())) {
                throw new ActionDeniedException("cannot withdraw to contract address " + toAddress);
            }
        }

        List<String> validAddresses = this.getValidTargetAddresses(chain);
        boolean validAddress = ObjectUtils.containsAddress(validAddresses, toAddress);
        Utils.check(validAddress, "invalid address");

        long gasPrice = chainService.getGasPrice(chain);
        long gasLimit = chainService.getGasLimit(chain, currency);
        long nonce = chainService.getPendingNonce(chain, requestDto.getFromAddress()).longValue();
        long sleepFor = 0;
        return this.sendTransaction(chain, FundCollectOrderType.FROM_SYSTEM_TO_SYSTEM, 0L, requestDto.getCurrency(),
                requestDto.getFromAddress(), requestDto.getToAddress(), requestDto.getAmount(), gasPrice, gasLimit, nonce, sleepFor, requestDto.getComments());
    }

    @Override
    public AddressCollectOrderHisDto createFundCollectOrder(AddressCollectOrderRequestDto addressCollectOrderRequestDto) {
        log.info("createFundCollectOrder addressCollectOrderRequestDto={}", addressCollectOrderRequestDto);
        Chain chain = Chain.fromCode(addressCollectOrderRequestDto.getChain());
        Utils.check(Chain.SUPPORT_LIST.contains(chain), ErrorCode.ACCOUNT_INVALID_CHAIN);

        int type = addressCollectOrderRequestDto.getType();
        String currency = addressCollectOrderRequestDto.getCurrency();
        long gasPrice = addressCollectOrderRequestDto.getGasPrice();
        // long gasLimit = chainService.getGasLimit(chain, currency);
        String address = addressCollectOrderRequestDto.getAddress();
        List<AddressCollectOrderRequestDto.AddressOrderDetail> list = addressCollectOrderRequestDto.getList();

        FundCollectOrderType orderType = FundCollectOrderType.fromCode(type);
        Utils.check(orderType == FundCollectOrderType.FROM_USER_TO_SYSTEM || orderType == FundCollectOrderType.FROM_SYSTEM_TO_USER, "invalid order type");
        Utils.check(currency != null && currency.length() > 0, "invalid currency");
        Utils.check(AddressUtils.validate(chain, address), "invalid address");
        Utils.check(gasPrice > 0, "invalid gas price");
        Utils.check(list != null && list.size() > 0, "missing details");

        List<String> validAddresses = getValidTargetAddresses(chain);

        list.forEach(e -> {
            Utils.check(AddressUtils.validate(chain, e.getAddress()), "invalid user address");
            boolean validSystemAddress = ObjectUtils.containsAddress(validAddresses, address);
            Utils.check(validSystemAddress, "invalid system address");
            boolean validUserAddress = ObjectUtils.containsAddress(validAddresses, e.getAddress());
            Utils.check(validUserAddress, "invalid user address");
            Utils.check(e.getAmount() != null && e.getAmount().signum() > 0, "invalid amount");

//            if (orderType == FundCollectOrderType.FROM_USER_TO_SYSTEM) {
//                boolean validAddress = ObjectUtils.containsAddress(validAddresses, address);
//                Utils.check(validAddress, "invalid address");
//            } else {
//                boolean validAddress = ObjectUtils.containsAddress(validAddresses, e.getAddress());
//                Utils.check(validAddress, "invalid address");
//            }
        });

        ChainContractDto chainContractDto = chainContractService.get(addressCollectOrderRequestDto.getChain(), currency);
        Utils.check(Objects.equals(currency, chain.getNativeToken()) || chainContractDto != null, "unknown currency");

        List<SystemWalletAddressDto> systemWalletAddresses = systemWalletAddressService.getAll()
                .stream()
                .filter(e -> e.getChain() == chain.getCode())
                .collect(Collectors.toList());
        // 目标地址必须是已知的系统地址地址
        Utils.check(systemWalletAddresses.stream().anyMatch(e -> ObjectUtils.isAddressEquals(address, e.getAddress())), "unknown address");

        AddressCollectOrderHis orderHis = AddressCollectOrderHis.builder()
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .version(AccountConstants.DEFAULT_VERSION)
                .type(type)
                .address(address)
                .currency(currency)
                .gasPrice(gasPrice)
                .feeAmount(BigDecimal.ZERO)
                .amount(BigDecimal.ZERO)
                .status(FundCollectOrderStatus.PROCESSING.getCode())
                .chain(chain)
                .build();
        addressCollectHisService.createOrderHistory(orderHis);
        long orderId = orderHis.getId();

        asyncService.execute(() -> processChildTransactions(chain, addressCollectOrderRequestDto, orderId));

        return ObjectUtils.copy(orderHis, new AddressCollectOrderHisDto());
    }


    private List<String> getValidTargetAddresses(Chain chain) {
        List<String> list = Lists.newLinkedList();

        // 已分配的用户地址 (使用map)
        List<ChainDepositAddress> chainDepositAddresses = chainDepositAddressMapper.getAssignedAddresses(Chain.mapChain(chain).getCode());
        for (ChainDepositAddress cda : chainDepositAddresses) {
            list.add(cda.getAddress());
        }

        // 系统钱包中有私钥的地址以及冷钱包
        List<SystemWalletAddressDto> systemWalletAddressDtos = systemWalletAddressService.getAll()
                .stream()
                .filter(e -> e.getChain() == chain.getCode())
                .collect(Collectors.toList());
        for (SystemWalletAddressDto swad : systemWalletAddressDtos) {
            WalletAddressType wat = WalletAddressType.fromCode(swad.getType());
            if (wat != null) {
                if (wat.isRequirePrivateKey() || wat == WalletAddressType.COLD || wat == WalletAddressType.KINE_TREASURY || wat == WalletAddressType.KUSD_VAULT) {
                    list.add(swad.getAddress());
                }
            }
        }

        return list;
    }


    private AddressCollectHisDto sendTransaction(Chain chain, FundCollectOrderType type, long orderId, String currency, String fromAddress, String toAddress, BigDecimal amount, long gasPrice, long gasLimit,
                                                 long nonce, long sleepFor, String comments) {
        int status = ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode();
        RawTransactionDto tx = null;
        try {
            tx = chainService.internalSendTransaction(chain, currency, fromAddress, toAddress, amount, gasPrice, gasLimit, nonce, sleepFor);
        } catch (Exception e) {
            log.error("sendTransaction error", e);
            status = ChainCommonStatus.TRANSACTION_FAILED.getCode();
        }

        AddressCollectHis his = AddressCollectHis.builder()
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .version(AccountConstants.DEFAULT_VERSION)
                .orderId(orderId)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .txToAddress(Optional.ofNullable(tx).map(RawTransactionDto::getTo).orElse(""))
                .currency(currency)
                .amount(amount)
                .gasPrice(gasPrice)
                .gasLimit(gasLimit)
                .txFee(BigDecimal.ZERO)
                .blockNumber(0L)
                .blockHash("")
                .txHash(Optional.ofNullable(tx).map(RawTransactionDto::getTxnHash).orElse(""))
                .txValue(Optional.ofNullable(tx).map(e -> e.getValue().toString()).orElse("0"))
                .nonce(Optional.ofNullable(tx).map(e -> e.getNonce().toString()).orElse("0"))
                .status(status)
                .comments(comments != null ? comments : "")
                .chain(chain)
                .build();
        addressCollectHisService.createHistory(his);
        // add new chain transaction data his
        ChainTxnData data = ChainTxnData.builder()
                .version(AccountConstants.DEFAULT_VERSION)
                .appId(his.getId())
                .data(Optional.ofNullable(tx).map(RawTransactionDto::getData).orElse(""))
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .build();
        switch (type) {
            case FROM_USER_TO_SYSTEM:
                // 资金归集
                data.setAppType(ChainTxnDataAppType.CASH_COLLECT);
                break;
            case FROM_SYSTEM_TO_USER:
                // Gas 划转
                data.setAppType(ChainTxnDataAppType.GAS_TRANSFER);
                break;
            case FROM_SYSTEM_TO_SYSTEM:
                data.setAppType(ChainTxnDataAppType.HOT_WALLET_TRANSFER);
                break;
            default:
                throw new AccountException(ErrorCode.ACCOUNT_BUSINESS_ERROR, "invalid fund collect order type");

        }
        transactionDataMapper.insert(data);
        return ObjectUtils.copy(his, new AddressCollectHisDto());
    }

    @Override
    public void createBalanceGet(Chain chain) {
        Utils.check(Chain.SUPPORT_LIST.contains(chain), ErrorCode.ACCOUNT_INVALID_CHAIN);

        BalanceGetStatusDto balanceGetStatusDto = getBalanceGetStatusDto(chain);

        // 设置新的起始时间，结束时间设置为0
        balanceGetStatusDto.setStartTime(System.currentTimeMillis());
        balanceGetStatusDto.setEndTime(0L);
        balanceGetStatusDto.setChain(chain.getCode());
        RBucket<String> bucket = client.getBucket(RedisKeys.getFundCollectBalanceGetStatus(chain.getCode()), StringCodec.INSTANCE);
        bucket.set(JsonUtils.writeValue(balanceGetStatusDto));

        asyncService.execute(() -> this.fetchAndCacheBalance(balanceGetStatusDto));
    }

    private void fetchAndCacheBalance(BalanceGetStatusDto balanceGetStatusDto) {
        log.info("fetchAndCacheBalance balanceGetStatusDto={}", balanceGetStatusDto);
        try {
            Chain chain = Chain.fromCode(balanceGetStatusDto.getChain());
            List<AddressBalanceDto> list = getPendingCollectBalances(chain);
            RBucket<String> bucket = client.getBucket(RedisKeys.getFundCollectBalanceValue(chain.getCode()), StringCodec.INSTANCE);
            bucket.set(JsonUtils.writeValue(list));
        } catch (Exception e) {
            log.error("fetchAndCacheBalance", e);
        }

        balanceGetStatusDto.setEndTime(System.currentTimeMillis());
        RBucket<String> bucket = client.getBucket(RedisKeys.getFundCollectBalanceGetStatus(balanceGetStatusDto.getChain()), StringCodec.INSTANCE);
        bucket.set(JsonUtils.writeValue(balanceGetStatusDto));
        log.info("fetchAndCacheBalance balanceGetStatusDto={}", balanceGetStatusDto);
    }

    @Override
    public BalanceGetStatusDto getBalanceGetStatusDto(Chain chain) {
        RBucket<String> bucket = client.getBucket(RedisKeys.getFundCollectBalanceGetStatus(chain.getCode()), StringCodec.INSTANCE);
        String json = bucket.get();
        return (json != null && json.length() > 0)
                ? JsonUtils.readValue(json, BalanceGetStatusDto.class)
                : BalanceGetStatusDto.builder().startTime(0L).endTime(0L).build();
    }

    @Override
    public List<AddressBalanceDto> getBalances(Chain chain, String currency) {
        RBucket<String> bucket = client.getBucket(RedisKeys.getFundCollectBalanceValue(chain.getCode()), StringCodec.INSTANCE);
        String json = bucket.get();
        List<AddressBalanceDto> list = (json != null && json.length() > 0)
                ? JsonUtils.readValue(json, new TypeReference<List<AddressBalanceDto>>() {
        })
                : Lists.newArrayList();
        // 限定返回地址的条数
        int limit = Integer.parseInt(systemConfigService.getValue(AccountSystemConfig.FUND_COLLECT_BALANCE_LIMIT, "500"));

        return list.stream()
                .sorted((a, b) -> b.getBalances().getOrDefault(currency, BigDecimal.ZERO).compareTo(a.getBalances().getOrDefault(currency, BigDecimal.ZERO)))
                .limit(limit)
                .collect(Collectors.toList());
    }


    private void processChildTransactions(Chain chain, AddressCollectOrderRequestDto addressCollectOrderRequestDto, long orderId) {
        int type = addressCollectOrderRequestDto.getType();
        String currency = addressCollectOrderRequestDto.getCurrency();
        long gasPrice = addressCollectOrderRequestDto.getGasPrice();
        long gasLimit = chainService.getGasLimit(chain, currency);
        String address = addressCollectOrderRequestDto.getAddress();
        List<AddressCollectOrderRequestDto.AddressOrderDetail> list = addressCollectOrderRequestDto.getList();

        FundCollectOrderType orderType = FundCollectOrderType.fromCode(type);

        if (orderType == FundCollectOrderType.FROM_USER_TO_SYSTEM) {
            // 1.  如果是是用户地址归集到系统地址，就先减去这次归集的金额，无论成功失败 (此设计是为了加快MGT处理速度)
            Map<String, BigDecimal> amountMap = list.stream()
                    .collect(Collectors.toMap(AddressCollectOrderRequestDto.AddressOrderDetail::getAddress, AddressCollectOrderRequestDto.AddressOrderDetail::getAmount));

            RBucket<String> bucket = client.getBucket(RedisKeys.getFundCollectBalanceValue(chain.getCode()), StringCodec.INSTANCE);
            String json = bucket.get();
            List<AddressBalanceDto> balanceList = (json != null && json.length() > 0)
                    ? JsonUtils.readValue(json, new TypeReference<List<AddressBalanceDto>>() {
            })
                    : Lists.newArrayList();
            balanceList.forEach(e -> {
                //获取这个地址要归集的数量
                BigDecimal amount = amountMap.getOrDefault(e.getAddress(), BigDecimal.ZERO);
                if (amount.signum() > 0) {
                    // 获取这个币种原有的余额
                    BigDecimal initialAmount = e.getBalances().get(currency);
                    if (initialAmount.compareTo(amount) >= 0) {
                        // 减去要归集的金额
                        e.getBalances().put(currency, initialAmount.subtract(amount));
                    }
                }
            });
            // 更新redis
            bucket.set(JsonUtils.writeValue(balanceList));

            // 2. 发送交易，插入数据库
            for (int i = 0; i < list.size(); i++) {
                AddressCollectOrderRequestDto.AddressOrderDetail e = list.get(i);
                // 从用户地址划转到系统钱包地址, 获取用户地址的pendingNonce，发送交易后不需要sleep
                long nonce = chainService.getPendingNonce(chain, e.getAddress()).longValue();
                long sleepFor = 0;
                String comments = orderId + "/" + list.size() + "/" + (i + 1);
                sendTransaction(chain, FundCollectOrderType.FROM_USER_TO_SYSTEM, orderId, currency, e.getAddress(), address, e.getAmount(), gasPrice, gasLimit, nonce, sleepFor, comments);
            }
        } else {
            // 1.  如果是系统地址划转到用户地址归，就加上这次归集的金额，无论成功失败 (此设计是为了加快MGT处理速度)
            Map<String, BigDecimal> amountMap = list.stream()
                    .collect(Collectors.toMap(AddressCollectOrderRequestDto.AddressOrderDetail::getAddress, AddressCollectOrderRequestDto.AddressOrderDetail::getAmount));

            RBucket<String> bucket = client.getBucket(RedisKeys.getFundCollectBalanceValue(addressCollectOrderRequestDto.getChain()), StringCodec.INSTANCE);
            String json = bucket.get();
            List<AddressBalanceDto> balanceList = (json != null && json.length() > 0)
                    ? JsonUtils.readValue(json, new TypeReference<List<AddressBalanceDto>>() {
            })
                    : Lists.newArrayList();
            balanceList.forEach(e -> {
                //获取这个地址要归集的数量
                BigDecimal amount = amountMap.getOrDefault(e.getAddress(), BigDecimal.ZERO);
                if (amount.signum() > 0) {
                    // 获取这个币种原有的余额
                    BigDecimal initialAmount = e.getBalances().get(currency);
                    e.getBalances().put(currency, initialAmount.add(amount));
                }
            });
            // 更新redis
            bucket.set(JsonUtils.writeValue(balanceList));
            // 获取一个初始的pendingNonce
            long nonce = chainService.getPendingNonce(chain, address).longValue();
            // 发送每笔交易后sleep
            long sleepFor = 1000;
            for (int i = 0; i < list.size(); i++) {
                AddressCollectOrderRequestDto.AddressOrderDetail e = list.get(i);
                String comments = orderId + "/" + list.size() + "/" + (i + 1);
                // 从系统钱包地址划转到用户地址（打gas fee）
                AddressCollectHisDto o = sendTransaction(chain, FundCollectOrderType.FROM_SYSTEM_TO_USER, orderId, currency, address, e.getAddress(), e.getAmount(), gasPrice, gasLimit, nonce, sleepFor, comments);
                if (o.getStatus() == ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode()) {
                    nonce++;
                }
            }
        }
    }
}
