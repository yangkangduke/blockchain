package com.seeds.account.service;


import com.seeds.account.dto.*;
import com.seeds.common.enums.Chain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 热钱包归集
 *
 * @author yk
 */
public interface IAddressCollectService {

    /**
     * 1. 发起余额查询请求
     */
    void createBalanceGet(Chain chain);

    /**
     * 2. 查询余额插叙状态
     *
     * @return
     */
    BalanceGetStatusDto getBalanceGetStatusDto(Chain chain);

    /**
     * 3. 获取币种按照大小倒叙排列的列表
     *
     * @param chain
     * @param currency
     * @return
     */
    List<AddressBalanceDto> getBalances(Chain chain, String currency);
    /**
     * 处理状态
     */
    void scanCollect() throws Exception;


    /**
     * 扫描获取待归集资产余额
     */
    void scanPendingCollectBalances();

    /**
     *
     * @return
     */
    Map<Chain, Map<String, BigDecimal>> getPendingCollectBalances();

    /**
     * 创建钱包划转
     *
     * @param requestDto
     * @return
     */
    AddressCollectHisDto createFundCollect(FundCollectRequestDto requestDto);
    /**
     * 创建归集订单
     *
     * @param addressCollectOrderRequestDto
     * @return
     */
    AddressCollectOrderHisDto createFundCollectOrder(AddressCollectOrderRequestDto addressCollectOrderRequestDto);

}
