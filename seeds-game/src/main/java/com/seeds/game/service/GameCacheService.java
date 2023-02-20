package com.seeds.game.service;

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
    void putGameHeroRankCache(String gameServerId, Long heroId, Long score, Long accId);

    /**
     * 获取英雄排行
     * @param gameServerId  游戏服id
     * @param heroId  英雄id
     * @param accId  角色id
     * @return 排名
     */
    String getHeroRankCache(String gameServerId, Long heroId, Long accId);

}
