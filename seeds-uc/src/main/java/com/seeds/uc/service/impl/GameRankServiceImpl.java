package com.seeds.uc.service.impl;

import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.service.GameRankService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author yk
 * @date 2020/8/2
 */
@Slf4j
@Transactional
@Service
public class GameRankServiceImpl implements GameRankService {

    @Value("${uc.game.win.rank.expire:10}")
    private Integer winRankExpireAfter;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RemoteGameService adminRemoteGameService;

    @Override
    public List<GameWinRankResp.GameWinRank> winInfo(GameWinRankReq query) {
        // 先从redis缓存中拿排行榜数据
        RBucket<List<GameWinRankResp.GameWinRank>> bucket = redissonClient.getBucket(UcRedisKeysConstant.getGameWinRankTemplate(query.getGameId().toString()));
        List<GameWinRankResp.GameWinRank> data = bucket.get();
        if (data != null) {
            return data;
        }
        // 请求游戏方获取排行榜数据
        GenericDto<List<GameWinRankResp.GameWinRank>> result = adminRemoteGameService.winRankInfo(query);
        if (!result.isSuccess()) {
            throw new GenericException("Failed to get the win list, please wait and try again!");
        }
        // 设置redis排行榜缓存
        List<GameWinRankResp.GameWinRank> newData = result.getData();
        bucket.set(newData, winRankExpireAfter, TimeUnit.MINUTES);
        return newData;
    }
}
