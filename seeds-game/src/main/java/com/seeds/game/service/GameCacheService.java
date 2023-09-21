package com.seeds.game.service;

import java.math.BigDecimal;

/**
 * redis缓存
 *
 * @author hang.yu
 * @date 2022/2/16
 */
public interface GameCacheService {

    /**
     * 缓存英雄排行
     * @param gameServerId  游戏服id
     * @param heroId  英雄id
     * @param score  英雄积分
     * @param accId  角色id
     */
    void putGameHeroRankCache(String gameServerId, Long heroId, BigDecimal score, Long accId);

    /**
     * 获取英雄排行
     * @param gameServerId  游戏服id
     * @param heroId  英雄id
     * @param accId  角色id
     * @return 排名
     */
    String getHeroRankCache(String gameServerId, Long heroId, Long accId);


    /**
     * 获取美元汇率
     * @param currency  币种
     * @return 美元汇率
     */
    String getUsdRate(String currency);

    /**
     * 缓存美元汇率
     * @param currency 币种
     * @param rate 汇率
     */
    void putUsdRate(String currency, String rate);

}