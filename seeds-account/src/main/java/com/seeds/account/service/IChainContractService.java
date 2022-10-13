package com.seeds.account.service;


import com.seeds.account.dto.ChainContractDto;
import com.seeds.common.enums.Chain;

import java.util.List;
import java.util.Map;

public interface IChainContractService {

    /**
     * 直接从数据库读取所有的合约配置
     * @return
     */
    List<ChainContractDto> loadAll();

    /**
     * 从缓存获取
     * @return
     */
    List<ChainContractDto> getAll();

    /**
     * 从缓存获取
     * @return
     */
    List<ChainContractDto> getAllByChain(Chain chain);

    /**
     * 从缓存获取
     * @return
     */
    Map<String, ChainContractDto> getAllMap();

    /**
     * 从缓存获取单个
     * @param chain
     * @param currency
     * @return
     */
    ChainContractDto get(int chain, String currency);

    /**
     * 插入新的
     * @param chainContractDto
     */
    void insert(ChainContractDto chainContractDto);

    /**
     * 更新
     * @param chainContractDto
     */
    void update(ChainContractDto chainContractDto);
}
