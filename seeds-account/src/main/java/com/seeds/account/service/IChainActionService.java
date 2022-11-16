package com.seeds.account.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplaceDto;
import com.seeds.account.dto.ChainTxnReplayDto;
import com.seeds.account.dto.SystemWalletAddressDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
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

    /**
     * 交易替换
     *
     * @throws Exception
     * @return
     */
    Boolean replayTransaction(ChainTxnReplayDto chainTxnReplayDto) throws Exception ;

    /**
     * 获取链上原始交易list
     * @param req
     * @return
     */
    IPage<ChainTxnDto> getTxnList(ChainTxnPageReq req);

    /**
     * 获取链上替换交易list
     * @param req
     * @return
     */
    IPage<ChainTxnDto> getTxnReplaceList(ChainTxnPageReq req);

    /**
     * 发起链上transaction replacement
     *
     * @param chainTxnReplaceDto
     */
    Long replaceTransaction(ChainTxnReplaceDto chainTxnReplaceDto) throws Exception ;

    void getAndMetricCurrentGasPriceOracle();

    /**
     * 创建热钱包地址
     * @param chain
     * @return
     */
    SystemWalletAddressDto createSystemWalletAddress(Chain chain) throws Exception;
}

