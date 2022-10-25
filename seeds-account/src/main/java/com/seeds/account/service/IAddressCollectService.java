package com.seeds.account.service;


import com.seeds.account.dto.AddressCollectHisDto;
import com.seeds.account.dto.AddressCollectOrderHisDto;
import com.seeds.account.dto.AddressCollectOrderRequestDto;
import com.seeds.account.dto.FundCollectRequestDto;
import com.seeds.common.enums.Chain;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 热钱包归集
 *
 * @author yk
 */
public interface IAddressCollectService {

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
