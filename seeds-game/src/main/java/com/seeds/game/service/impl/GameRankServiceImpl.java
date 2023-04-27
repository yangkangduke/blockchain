package com.seeds.game.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.game.config.warblade.GameWarbladeConfig;
import com.seeds.game.entity.ServerRegionEntity;
import com.seeds.game.service.GameFileService;
import com.seeds.game.service.GameRankService;
import com.seeds.game.service.IServerRegionService;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author yk
 * @date 2020/8/2
 */
@Slf4j
@Service
public class GameRankServiceImpl implements GameRankService {

    public static final String GAME_WINS_RANKING_FILE = "GameWinsRanking.png";

    @Value("${uc.game.win.rank.expire:10}")
    private Integer winRankExpireAfter;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private GameFileService gameFileService;

    @Autowired
    private GameWarbladeConfig gameWarbladeConfig;

    @Autowired
    private IServerRegionService serverRegionService;

    @Override
    public List<GameWinRankResp.GameWinRank> winInfo(GameWinRankReq query, Boolean useCache) {
        // 先从redis缓存中拿排行榜数据
        GameWinRankResp resp;
        RBucket<String> bucket = null;
        List<GameWinRankResp.GameWinRank> cacheRankList = new ArrayList<>();
        if (useCache) {
            bucket = redissonClient.getBucket(UcRedisKeysConstant.getGameWinRankTemplate(query.getGameId().toString(), query.getSortType().toString()));
            String data = bucket.get();
            if (StringUtils.isNotBlank(data)) {
                resp = JSONUtil.toBean(data, GameWinRankResp.class);
                cacheRankList = resp.getInfos();
                // 判断是否过期
                if (resp.getExpireTime() > System.currentTimeMillis()) {
                    return cacheRankList;
                }
            }
        }
        // 请求游戏方获取排行榜数据
        List<ServerRegionEntity> servers = serverRegionService.list();
        if (CollectionUtils.isEmpty(servers)) {
            return Collections.emptyList();
        }
        Map<String, ServerRegionEntity> serverRegionMap = servers.stream().filter(p -> StringUtils.isNotBlank(p.getInnerHost())).collect(Collectors.toMap(ServerRegionEntity::getInnerHost, p -> p, (a, b) -> a));
        List<GameWinRankResp.GameWinRank> rankList = new ArrayList<>();
        try {
            for (String innerHost : serverRegionMap.keySet()) {
                String rankUrl = innerHost + gameWarbladeConfig.getPlayerWinRank();
                String params = String.format("startRow=%s&endRow=%s", query.getStartRow(), query.getEndRow() * 2);
                rankUrl = rankUrl + "?" + params;
                log.info("开始请求游戏胜场排行榜数据， url:{}， params:{}", rankUrl, params);
                HttpResponse response = HttpRequest.get(rankUrl)
                        .timeout(10 * 1000)
                        .header("Content-Type", "application/json")
                        .execute();
                String body = response.body();
                log.info("请求游戏胜场排行榜数据返回，result:{}", body);
                if (!response.isOk()) {
                    log.error("Failed to get the win list from game");
                    throw new GenericException("Failed to get the win list");
                }
                resp = JSONUtil.toBean(body, GameWinRankResp.class);
                if (!"OK".equalsIgnoreCase(resp.getRet())) {
                    log.error("Get the win list from game failed");
                    throw new GenericException("Get the win list from game failed");
                }
                List<GameWinRankResp.GameWinRank> infos = resp.getInfos();
                if (!CollectionUtils.isEmpty(infos)) {
                    infos.forEach(p -> {
                        ServerRegionEntity serverRegion = serverRegionMap.get(innerHost);
                        if (serverRegion != null) {
                            p.setRegionName(serverRegion.getRegionName());
                        }
                    });
                }
                rankList.addAll(infos);
            }
            if (CollectionUtils.isEmpty(rankList)) {
                return Collections.emptyList();
            }
            Map<String, GameWinRankResp.GameWinRank> rankMap = new HashMap<>(rankList.size());
            for (GameWinRankResp.GameWinRank rank : rankList) {
                String key = rank.getAccName();
                GameWinRankResp.GameWinRank mapRank = rankMap.get(key);
                // 头像url
                rank.setPortraitUrl(gameFileService.getFileUrl("game/" + rank.getPortraitId() + GAME_WINS_RANKING_FILE));
                if (mapRank == null) {
                    rankMap.put(key, rank);
                } else {
                    // 分数高的服务器数据展示在排行榜
                    if (mapRank.getScore() < rank.getScore()) {
                        rankMap.put(key, rank);
                    }
                }
            }
            rankList = sortBySortType(query.getSortType(), rankMap, query.getEndRow());

            // 设置redis排行榜缓存
            if (!CollectionUtils.isEmpty(rankList) && bucket != null) {
                resp = new GameWinRankResp();
                resp.setInfos(rankList);
                resp.setExpireTime(System.currentTimeMillis() + winRankExpireAfter * 60 * 1000);
                bucket.set(JSONUtil.toJsonStr(resp), winRankExpireAfter, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            log.error("请求游戏胜场排行榜数据失败，message:{}", e.getMessage());
            return cacheRankList;
        }
        return rankList;
    }

    List<GameWinRankResp.GameWinRank> sortBySortType(Integer sortType, Map<String, GameWinRankResp.GameWinRank> rankMap, Integer limit) {
        switch (sortType) {
            case 2:
                return rankMap.values().stream()
                        .sorted(Comparator.comparing(GameWinRankResp.GameWinRank::getWinNum).reversed())
                        .limit(limit).collect(Collectors.toList());
            case 3:
                return rankMap.values().stream()
                        .sorted(Comparator.comparing(GameWinRankResp.GameWinRank::getMaxSeqWin).reversed())
                        .limit(limit).collect(Collectors.toList());
            case 4:
                return rankMap.values().stream()
                        .sorted(Comparator.comparing(GameWinRankResp.GameWinRank::getFightNum).reversed())
                        .limit(limit).collect(Collectors.toList());
            case 1:
            default:
                return rankMap.values().stream()
                        .sorted(Comparator.comparing(GameWinRankResp.GameWinRank::getScore).reversed())
                        .limit(limit).collect(Collectors.toList());
        }
    }
}
