package com.seeds.account.chain.service.impl;

import com.seeds.account.chain.dto.ChainTransaction;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.stream.Collectors;

/**
 * Convert Specific Chain API DTO to Common Chain DTO
 * @author ray
 */
public abstract class Web3ChainConverter {

    public ChainTransaction toChainTransaction(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        // TODO;
        return ChainTransaction.builder().txHash(transaction.getHash()).build();
    }

    public ChainTransactionReceipt toChainTransactionReceipt(TransactionReceipt transactionReceipt) {
        if (transactionReceipt == null) {
            return null;
        }
        return ChainTransactionReceipt.builder()
                .status(transactionReceipt.isStatusOK())
                .blockHash(transactionReceipt.getBlockHash())
                .blockNumber(transactionReceipt.getBlockNumber().longValue())
                .gasUsed(transactionReceipt.getGasUsed().longValue())
                .logs(transactionReceipt.getLogs().stream().map(this::convertLog).collect(Collectors.toList()))
                .revertReason(transactionReceipt.getRevertReason())
                .build();
    }

    public com.seeds.account.chain.dto.Log  convertLog(Log log) {
        com.seeds.account.chain.dto.Log  newLog = new com.seeds.account.chain.dto.Log ();
        BeanUtils.copyProperties(log, newLog);
        return newLog;
    }
}
