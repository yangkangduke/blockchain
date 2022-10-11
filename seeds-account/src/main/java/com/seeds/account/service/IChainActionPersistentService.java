package com.seeds.account.service;

import com.seeds.account.chain.dto.NativeChainBlockDto;
import com.seeds.account.chain.dto.NativeChainTransactionDto;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.common.enums.Chain;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * 所有的方法都是事务的
 *
 * @author milo
 *
 */
public interface IChainActionPersistentService {

    /**
     * 插入空闲地址
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    void insert(List<ChainDepositAddress> list);

    /**
     * 回滚区块
     * @param chain
     * @param blockNumber
     * @param blockHash
     */
    @Transactional(rollbackFor = Exception.class)
    void rollbackBlock(Chain chain, long blockNumber, String blockHash);

    /**
     * 处理新的区块：保存新区块，确认充币，上账，及财务流水，发通知
     * @param newBlockDto
     * @param assignedList
     * @param transactions
     * @param toSuspendedList
     * @param toConfirmList
     */
    @Transactional(rollbackFor = Exception.class)
    void processNewBlock(NativeChainBlockDto newBlockDto,
                         List<ChainDepositAddress> assignedList, List<NativeChainTransactionDto> transactions,
                         List<ChainDepositWithdrawHis> toSuspendedList, List<ChainDepositWithdrawHis> toConfirmList);

    /**
     * 确认单笔
     * @param transaction
     */
    @Transactional(rollbackFor = Exception.class)
    void confirmTransaction(ChainDepositWithdrawHis transaction);
}
