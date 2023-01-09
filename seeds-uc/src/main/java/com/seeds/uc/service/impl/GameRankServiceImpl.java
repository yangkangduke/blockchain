package com.seeds.uc.service.impl;

import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.service.GameRankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author yk
 * @date 2020/8/2
 */
@Slf4j
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
        RBucket<String> bucket = redissonClient.getBucket(UcRedisKeysConstant.getGameWinRankTemplate(query.getGameId().toString()));
        String data = bucket.get();
        GameWinRankResp resp = null;
        if (StringUtils.isNotBlank(data)) {
            resp = JSONUtil.toBean(data, GameWinRankResp.class);
            // 判断是否过期
            if (resp.getExpireTime() > System.currentTimeMillis()) {
                return resp.getInfos();
            }
        }
        // 请求游戏方获取排行榜数据
        GenericDto<List<GameWinRankResp.GameWinRank>> result;
        try {
            result = adminRemoteGameService.winRankInfo(query);
        } catch (Exception e) {
            if (resp == null) {
                throw e;
            }
            return resp.getInfos();
        }
        if (!result.isSuccess()) {
            if (resp == null) {
                throw new GenericException(result.getMessage());
            }
            return resp.getInfos();
        }
        // 设置redis排行榜缓存
        List<GameWinRankResp.GameWinRank> newData = result.getData();
        if (!CollectionUtils.isEmpty(newData)) {
            resp = new GameWinRankResp();
            resp.setInfos(newData);
            resp.setExpireTime(System.currentTimeMillis() + winRankExpireAfter * 60 * 1000);
            bucket.set(JSONUtil.toJsonStr(resp), winRankExpireAfter, TimeUnit.DAYS);
        }
        return newData;
    }
}
