package com.seeds.game.service.impl;

import com.seeds.game.constant.GameRedisKeys;
import com.seeds.game.service.GameCacheService;
import com.seeds.uc.constant.UcRedisKeysConstant;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;


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

    @Value("${uc.usd.rate.expire:1}")
    private Integer usdRateExpireAfter;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void putGameHeroRankCache(String gameServerId, Long heroId, BigDecimal score, Long accId) {
        // 保存到redis排行榜
        RScoredSortedSet<Object> sortedSet = redissonClient.getScoredSortedSet(GameRedisKeys.getGameHeroRankKey(gameServerId, heroId));
        // 添加到排行榜
        sortedSet.add(score.doubleValue(), accId);
        // 超过最大排名heroRankMax的排除
        if (sortedSet.size() > heroRankMax) {
            sortedSet.removeRangeByRank(0, sortedSet.size() - 1 - heroRankMax);
        }
    }

    @Override
    public String getHeroRankCache(String gameServerId, Long heroId, Long accId) {
        RScoredSortedSet<Object> sortedSet = redissonClient.getScoredSortedSet(GameRedisKeys.getGameHeroRankKey(gameServerId, heroId));
        Integer rank = sortedSet.revRank(accId);
        if (rank == null) {
            return heroRankMax + "+";
        } else {
            rank = rank + 1;
        }
        return rank.toString();
    }

    @Override
    public String getUsdRate(String currency) {
        String key = UcRedisKeysConstant.getUsdRateTemplate(currency);
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public void putUsdRate(String currency, String rate) {
        String key = UcRedisKeysConstant.getUsdRateTemplate(currency);
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(rate, usdRateExpireAfter, TimeUnit.HOURS);
    }
}