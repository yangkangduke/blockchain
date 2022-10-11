package com.seeds.account.service;

import org.redisson.api.RMap;

import java.util.List;

/**
 * @author guocheng
 * @date 2020/12/30
 */
public interface IFundingRateActionService {

    /**
     * 是否有资产在资金费结算中 - global结算锁
     *
     * @return
     */
    boolean isAnySettlingAsset();
}
