package com.seeds.account.service;


import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.SystemWalletAddressDto;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.common.enums.Chain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yk
 *
 */
public interface IChainDepositService {

    /**
     * 获取充币地址
     *
     * @param chain
     * @param userId
     * @param createIfNull
     * @return
     */
    String getDepositAddress(Chain chain, long userId, boolean createIfNull) throws Exception;

    /**
     * 从数据库获取所有
     * @return
     */
    List<DepositRuleDto> loadAll();

    /**
     * 获取充币地址的绑定关系
     *
     * @param chain
     * @param address
     * @return
     */
    ChainDepositAddress getByAddress(Chain chain, String address);

    /**
     * 从内存获取充币规则
     * @param chain
     * @param currency
     * @return
     */
    DepositRuleDto getDepositRule(Chain chain, String currency);

    /**
     * 从内存获取所有的充币规则
     *
     * @return
     */
    Map<String, DepositRuleDto> getAllDepositRuleMap();

    /**
     * 从内存获取充币规则
     *
     * @return
     */
    List<DepositRuleDto> getAllDepositRules();}
