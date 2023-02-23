package com.seeds.game.service.impl;

import com.seeds.game.constant.GameRedisKeys;
import com.seeds.game.service.GameCacheService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * redis缓存
 *
 * @author hang.yu
 * @date 2022/2/16
 */
@Slf4j
@Service
public class GameCacheServiceImpl implements GameCacheService {

    @Value("${game.hero.rank.max:100}")
    private Integer heroRankMax;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void putGameHeroRankCache(String gameServerId, Long heroId, BigDecimal score, Long accId) {
        // 保存到redis排行榜
        RScoredSortedSet<Object> sortedSet = redissonClient.getScoredSortedSet(GameRedisKeys.getGameHeroRankKey(gameServerId, heroId));
        int rank = sortedSet.addAndGetRevRank(score.doubleValue(), accId) + 1;
        if (rank > heroRankMax) {
            sortedSet.remove(accId);
        }
    }

    @Override
    public String getHeroRankCache(String gameServerId, Long heroId, Long accId) {
        RScoredSortedSet<Object> sortedSet = redissonClient.getScoredSortedSet(GameRedisKeys.getGameHeroRankKey(gameServerId, heroId));
        Integer rank = sortedSet.revRank(accId);
        if (rank == null) {
            return heroRankMax + "+";
        }
        return rank.toString();
    }
}