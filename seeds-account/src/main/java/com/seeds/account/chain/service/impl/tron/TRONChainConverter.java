package com.seeds.account.chain.service.impl.tron;

import com.seeds.account.chain.dto.ChainTransaction;
import com.seeds.account.chain.dto.ChainTransactionReceipt;
import com.seeds.account.chain.dto.Log;
import com.seeds.account.chain.dto.NativeChainBlockDto;
import org.springframework.stereotype.Component;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;

import java.util.stream.Collectors;

/**
 * Convert Specific Chain API DTO to Common Chain DTO
 *
 * @author ray
 */
@Component
public class TRONChainConverter {

    public ChainTransaction toChainTransaction(String txHash, Chain.Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return ChainTransaction.builder()
                .chain(com.seeds.common.enums.Chain.TRON)
                .txHash(txHash)
                .build();
    }

    public ChainTransactionReceipt toChainTransactionReceipt(Chain.Transaction transaction, Response.TransactionInfo transactionInfo, Response.BlockExtention blockExtention) {
        if (transactionInfo == null || !transactionInfo.hasReceipt()) {
            return null;
        }
        return ChainTransactionReceipt.builder()
                .status(transaction.getRet(0).getRet() == Chain.Transaction.Result.code.SUCESS && transaction.getRet(0).getContractRet().equals(Chain.Transaction.Result.contractResult.SUCCESS))
                .blockHash(ApiWrapper.toHex(blockExtention.getBlockid()))
                .blockNumber(transactionInfo.getBlockNumber())
                .gasUsed(transactionInfo.getFee())
                .logs(transactionInfo.getLogList().stream().map(this::convertLog).collect(Collectors.toList()))
                .revertReason(null)
                .build();
    }

    private Log convertLog(Response.TransactionInfo.Log log) {
        return Log.builder()
                .address(ApiWrapper.toHex(log.getAddress()))
                .blockHash(null)
                .blockNumber(null)
                .build();
    }

    public NativeChainBlockDto toNativeChainBlockDto(Response.BlockExtention block) {
        if(block == null){
            return null;
        }

        return NativeChainBlockDto.builder()
                .blockHash(ApiWrapper.toHex(block.getBlockid()))
                .blockNumber(block.getBlockHeader().getRawData().getNumber())
                // 取秒 跟ETH的blockTime保持单位一致
                .blockTime(block.getBlockHeader().getRawData().getTimestamp()/1000)
                .chain(com.seeds.common.enums.Chain.TRON)
                .parentHash(ApiWrapper.toHex(block.getBlockHeader().getRawData().getParentHash()))
                .build();
    }
}
