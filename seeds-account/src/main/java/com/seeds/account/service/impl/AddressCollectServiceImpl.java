package com.seeds.account.service.impl;

import com.seeds.account.anno.SingletonLock;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.enums.ChainCommonStatus;
import com.seeds.account.enums.ChainTxnReplaceAppType;
import com.seeds.account.enums.FundCollectOrderStatus;
import com.seeds.account.enums.FundCollectOrderType;
import com.seeds.account.ex.AccountException;
import com.seeds.account.mapper.AddressCollectHisMapper;
import com.seeds.account.mapper.AddressCollectOrderHisMapper;
import com.seeds.account.mapper.ChainTxnReplaceMapper;
import com.seeds.account.model.AddressCollectHis;
import com.seeds.account.model.AddressCollectOrderHis;
import com.seeds.account.model.ChainBlock;
import com.seeds.account.model.ChainTxnReplace;
import com.seeds.account.service.IAddressCollectService;
import com.seeds.account.service.IChainActionService;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressCollectServiceImpl implements IAddressCollectService {

    @Autowired
    ChainTxnReplaceMapper chainTxnReplaceMapper;
    @Autowired
    IChainActionService chainActionService;
    @Autowired
    AddressCollectHisMapper addressCollectHisMapper;
    @Autowired
    IChainService chainService;
    @Autowired
    AddressCollectOrderHisMapper addressCollectOrderHisMapper;

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
            Map<Long, List<ChainTxnReplace>> pendingReplaceMap = chainTxnReplaceMapper.getListByChainStatusAndType(collectOrder.getChain(),
                            ChainCommonStatus.TRANSACTION_ON_CHAIN,
                            FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode().equals(collectOrder.getType()) ? ChainTxnReplaceAppType.CASH_COLLECT : ChainTxnReplaceAppType.GAS_TRANSFER).stream()
                    .collect(Collectors.groupingBy(ChainTxnReplace::getAppId));
            // 查看是否所有的都已经处理完了
            if (collectHisList.stream().anyMatch(e -> e.getStatus() == ChainCommonStatus.TRANSACTION_ON_CHAIN.getCode()) ||
                    // 存在replace中的
                    !pendingReplaceMap.isEmpty()) {
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

}
