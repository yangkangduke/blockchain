package com.seeds.account.service;

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
