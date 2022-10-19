package com.seeds.account.service;

import com.seeds.account.enums.ChainTxnReplaceAppType;
import com.seeds.account.model.AddressCollectHis;
import com.seeds.account.model.ChainBlock;
import com.seeds.common.enums.Chain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IChainActionService {


    void processCollectSuccess(AddressCollectHis tx, long blockNumber, String blockHash, BigDecimal txFee, boolean isReplace);

    /**
     * 检查空闲地址的数量，不够的时候就创建新的
     *
     * @throws Exception
     */
    void scanAndCreateAddresses() throws Exception;

    /**
     * 扫描新块
     *
     * @throws Exception
     */
    void scanBlock() throws Exception;

    /**
     * 执行提币
     *
     * @throws Exception
     */
    void executeWithdraw() throws Exception;

    /**
     * 扫描提币状态
     *
     * @throws Exception
     */
    void scanWithdraw() throws Exception;

    /**
     * 扫描transaction replacement 状态
     *
     * @param replaceAppType
     * @throws Exception
     */
    void scanTxnReplace(ChainTxnReplaceAppType replaceAppType) throws Exception;

    Map<Chain, ChainBlock> getLatestBlock(List<Chain> chainList);


}

