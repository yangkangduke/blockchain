//package com.seeds.account.service;
//
//
//import com.seeds.account.dto.AssetPriceDto;
//import com.seeds.account.dto.PriceMinMaxDto;
//import com.seeds.account.listener.PriceUpdateListener;
//
//import java.math.BigDecimal;
//import java.util.Map;
//
///**
// *
// * @author yk
// *
// */
//public interface IPriceService {
//
//    /**
//     * 注册价格监听器
//     * @param listener
//     */
//    void register(PriceUpdateListener listener);
//
//    /**
//     * 删除价格监听器
//     * @param listener
//     */
//    void remove(PriceUpdateListener listener);
//
//    /**
//     * 获取最新的所有价格
//     * @return
//     */
//    Map<String, BigDecimal> getLatestPriceMap();
//
//    /**
//     * 获取最新更新时间
//     * @return
//     */
//    long getLastUpdateTime();
//
//    /**
//     * 获取最新的所有价格，并检查价格的时间有效性
//     * @return
//     */
//    Map<String, BigDecimal> getLatestPriceMapOnCheck();
//
//    /**
//     * 获取某个交易对最新的价格
//     * @param symbol
//     * @return
//     */
//    BigDecimal getLatestPrice(String symbol);
//
//    /**
//     * 接收价格更新
//     * @param aggregatedPrice
//     */
//    void onAggregatedPrice(AggregatedPrice aggregatedPrice);
//
//    /**
//     * 获取kine last price
//     */
//    BigDecimal getLastKinePrice();
//
//    /**
//     * 获取最后一次聚合价格
//     * @return
//     */
//    Map<String, AssetPriceDto> getLatestAssetPrice();
//
//    /**
//     * 获取单资产的价格，并检查有效期
//     * @param symbol
//     * @return
//     */
//    BigDecimal getLatestPriceOnCheck(String symbol);
//
//    PriceMinMaxDto getCachedMinMax(long userId, String symbol);
//
//    /**
//     * 获取交易对在时间窗口内最低和最高价格（计算爆仓时使用）
//     * @param symbol
//     * @param timeWindow
//     * @return
//     */
//    PriceMinMaxDto getCachedMinMax(String symbol, long timeWindow);
//}
