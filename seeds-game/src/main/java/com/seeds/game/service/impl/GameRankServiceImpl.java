package com.seeds.game.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.GameRankNftPageReq;
import com.seeds.game.dto.request.GameRankNftSkinReq;
import com.seeds.game.dto.request.GameRankStatisticPageReq;
import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.*;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.config.warblade.GameWarbladeConfig;
import com.seeds.game.dto.response.*;
import com.seeds.game.entity.*;
import com.seeds.game.enums.SortTypeEnum;
import com.seeds.game.mapper.NftMarketOrderMapper;
import com.seeds.game.service.*;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.feign.UserCenterFeignClient;
import com.seeds.uc.model.UcUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.RoundingMode;
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

    public static final String GAME_ROLE_PORTRAIT_FILE = "GameRolePortrait.png";

    @Value("${uc.game.win.rank.expire:10}")
    private Integer winRankExpireAfter;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private GameFileService gameFileService;

    @Autowired
    private IServerRoleService serverRoleService;

    @Autowired
    private GameWarbladeConfig gameWarbladeConfig;

    @Autowired
    private INftAttributeService nftAttributeService;

    @Autowired
    private IServerRegionService serverRegionService;

    @Autowired
    private NftMarketOrderMapper nftMarketOrderMapper;

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @Autowired
    private IServerRoleStatisticsService serverRoleStatisticsService;

    @Override
    public List<GameWinRankResp.GameWinRank> winInfo(GameWinRankReq query, Boolean useCache) {
        Long currentUser = UserContext.getCurrentUserIdNoThrow();
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
                    cacheRankList.forEach(p -> {
                        if (p.getUserId() != null) {
                            p.setIsOwner(p.getUserId().equals(currentUser) ? 1 : 0);
                        }
                    });
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
            Set<Long> roleIds = rankList.stream().map(GameWinRankResp.GameWinRank::getAccID).collect(Collectors.toSet());
            Map<Long, ServerRoleEntity> gameRoleMap = serverRoleService.queryMapById(roleIds);
            Map<String, GameWinRankResp.GameWinRank> rankMap = new HashMap<>(rankList.size());
            for (GameWinRankResp.GameWinRank rank : rankList) {
                ServerRoleEntity serverRole = gameRoleMap.get(rank.getAccID());
                if (serverRole != null) {
                    rank.setUserId(serverRole.getUserId());
                    rank.setIsOwner(serverRole.getUserId().equals(currentUser) ? 1 : 0);
                }
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

    @Override
    public IPage<GameRankStatisticResp> statisticPage(GameRankStatisticPageReq query) {
        Long currentUser = UserContext.getCurrentUserIdNoThrow();
        LambdaQueryWrapper<ServerRoleStatisticsEntity> queryWrap = new QueryWrapper<ServerRoleStatisticsEntity>().lambda()
                .eq(ServerRoleStatisticsEntity::getGameServerId, query.getGameServerId());
        buildOrderCondition(query.getSortField(), query.getSortType(), queryWrap);
        Page<ServerRoleStatisticsEntity> page = serverRoleStatisticsService.page(new Page<>(query.getCurrent(), query.getSize()), queryWrap);
        List<ServerRoleStatisticsEntity> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> roleIds = records.stream().map(ServerRoleStatisticsEntity::getRoleId).collect(Collectors.toSet());
        Map<Long, ServerRoleEntity> gameRoleMap = serverRoleService.queryMapById(roleIds);
        return page.convert(p -> {
            GameRankStatisticResp resp = new GameRankStatisticResp();
            BeanUtils.copyProperties(p, resp);
            resp.setWinRate(p.getWinRate().scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP) + "%");
            ServerRoleEntity serverRole = gameRoleMap.get(p.getRoleId());
            if (serverRole != null) {
                resp.setIsOwner(serverRole.getUserId().equals(currentUser) ? 1 : 0);
                resp.setPortraitUrl(gameFileService.getFileUrl("game/" + serverRole.getPortraitId() + GAME_ROLE_PORTRAIT_FILE));
                resp.setRoleName(serverRole.getName());
            }
            return resp;
        });
    }

    @Override
    public IPage<GameRankEquipResp> equipPage(GameRankNftPageReq query) {
        Long currentUser = UserContext.getCurrentUserIdNoThrow();
        query.setSortTypeStr(GameRankNftPageReq.convert(query.getSortType()));
        Page<GameRankEquipResp> page = new Page<>(query.getCurrent(), query.getSize());
        IPage<GameRankEquipResp> equipPage = nftMarketOrderMapper.equipPage(page, query);
        List<GameRankEquipResp> records = equipPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<String> publicAddress = records.stream().map(GameRankEquipResp::getPublicAddress).collect(Collectors.toSet());
        Map<String, UcUser> userMap = getUserMapByAddress(publicAddress);
        return page.convert(p -> {
            UcUser ucUser = userMap.get(p.getPublicAddress());
            if (ucUser != null) {
                p.setIsOwner(ucUser.getId().equals(currentUser) ? 1 : 0);
                p.setCollector(ucUser.getNickname());
            }
            return p;
        });
    }

    @Override
    public IPage<GameRankItemResp> itemPage(GameRankNftPageReq query) {
        Long currentUser = UserContext.getCurrentUserIdNoThrow();
        query.setSortTypeStr(GameRankNftPageReq.convert(query.getSortType()));
        Page<GameRankItemResp> page = new Page<>(query.getCurrent(), query.getSize());
        IPage<GameRankItemResp> itemPage = nftMarketOrderMapper.itemPage(page, query);
        List<GameRankItemResp> records = itemPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<String> publicAddress = records.stream().map(GameRankItemResp::getPublicAddress).collect(Collectors.toSet());
        Map<String, UcUser> userMap = getUserMapByAddress(publicAddress);
        return page.convert(p -> {
            UcUser ucUser = userMap.get(p.getPublicAddress());
            if (ucUser != null) {
                p.setCollector(ucUser.getNickname());
                p.setIsOwner(ucUser.getId().equals(currentUser) ? 1 : 0);
            }
            return p;
        });
    }

    @Override
    public IPage<GameRankHeroResp> heroPage(GameRankNftPageReq query) {
        Long currentUser = UserContext.getCurrentUserIdNoThrow();
        query.setSortTypeStr(GameRankNftPageReq.convert(query.getSortType()));
        Page<GameRankHeroResp> page = new Page<>(query.getCurrent(), query.getSize());
        IPage<GameRankHeroResp> heroPage = nftMarketOrderMapper.heroPage(page, query);
        List<GameRankHeroResp> records = heroPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Map<String, UcUser> userMap = getUserMapByAddress(records.stream().map(GameRankHeroResp::getPublicAddress).collect(Collectors.toSet()));
        return page.convert(p -> {
            UcUser ucUser = userMap.get(p.getPublicAddress());
            if (ucUser != null) {
                p.setIsOwner(ucUser.getId().equals(currentUser) ? 1 : 0);
                p.setCollector(ucUser.getNickname());
            }
            return p;
        });
    }

    @Override
    public List<GameRankNftSkinResp.GameRankNftSkin> nftSkin(GameRankNftSkinReq query) {
        Long currentUser = UserContext.getCurrentUserIdNoThrow();
        // 先从redis缓存中拿排行榜数据
        RBucket<String> bucket = redissonClient.getBucket(UcRedisKeysConstant.UC_GAME_NFT_SKIN_RANK_TEMPLATE);
        String data = bucket.get();
        Map<Long, Integer> scoreMap = new HashMap<>();
        if (StringUtils.isNotBlank(data)) {
            GameRankNftSkinResp resp = JSONUtil.toBean(data, GameRankNftSkinResp.class);
            List<GameRankNftSkinResp.GameRankNftSkin> cacheList = resp.getInfos();
            // 判断是否过期
            if (resp.getExpireTime() > System.currentTimeMillis()) {
                cacheList.forEach(p -> {
                    if (p.getUserId() != null) {
                        p.setIsOwner(p.getUserId().equals(currentUser) ? 1 : 0);
                    }
                });
                return cacheList;
            }
            scoreMap = cacheList.stream().collect(Collectors.toMap(GameRankNftSkinResp.GameRankNftSkin::getNftId, GameRankNftSkinResp.GameRankNftSkin::getRank));
        }
        Map<Long, GameRankNftSkinResp.GameRankNftSkin> map = new HashMap<>();
        Integer number = query.getEndRow();
        // victory子榜单 系数1 积分200
        List<NftAttributeEntity> victoryRank = nftAttributeService.querySkinRankVictory(number);
        nftAttributeService.calculateSkinRankScore(victoryRank, map, 1, -1);
        // lose子榜单 系数-1 积分-200
        List<NftAttributeEntity> loseRank = nftAttributeService.querySkinRankLose(number);
        nftAttributeService.calculateSkinRankScore(loseRank, map, -1, -1);
        // max streak子榜单 系数2 积分400
        List<NftAttributeEntity> maxStreakRank = nftAttributeService.querySkinRankMaxStreak(number);
        nftAttributeService.calculateSkinRankScore(maxStreakRank, map, 2, -1);
        // capture子榜单 系数0.5 积分100
        List<NftAttributeEntity> captureRank = nftAttributeService.querySkinRankCapture(number);
        nftAttributeService.calculateSkinRankScore(captureRank, map, 0.5, -1);
        // killing spree子榜单 系数1 积分200
        List<NftAttributeEntity> killingSpreeRank = nftAttributeService.querySkinRankKillingSpree(number);
        nftAttributeService.calculateSkinRankScore(killingSpreeRank, map, 1, -1);
        // goblin kill子榜单 系数0.5 积分100
        List<NftAttributeEntity> goblinKillRank = nftAttributeService.querySkinRankGoblinKill(number);
        nftAttributeService.calculateSkinRankScore(goblinKillRank, map, 0.5, -1);
        // death by slaying子榜单 系数-0.5 积分-100
        List<NftAttributeEntity> slayingRank = nftAttributeService.querySkinRankSlaying(number);
        nftAttributeService.calculateSkinRankScore(slayingRank, map, -0.5, -1);
        // death by goblin子榜单 系数-0.5 积分-100
        List<NftAttributeEntity> goblinRank = nftAttributeService.querySkinRankGoblin(number);
        nftAttributeService.calculateSkinRankScore(goblinRank, map, -0.5, -1);
        Set<Long> nftIds = new HashSet<>();
        Collection<GameRankNftSkinResp.GameRankNftSkin> values = map.values();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        List<GameRankNftSkinResp.GameRankNftSkin> respList = values.stream().filter(p -> p.getScore() > 0)
                .sorted(Comparator.comparing(GameRankNftSkinResp.GameRankNftSkin::getScore).reversed())
                .limit(number).peek(p -> nftIds.add(p.getNftId())).collect(Collectors.toList());
        Map<Long, NftPublicBackpackEntity> backpackMap = nftPublicBackpackService.queryMapByEqNftIds(nftIds);
        Set<Long> userIds = backpackMap.values().stream().map(NftPublicBackpackEntity::getUserId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, UcUser> userMap = getUserMapByIds(userIds);
        int rank = 1;
        for (GameRankNftSkinResp.GameRankNftSkin nftSkin : respList) {
            nftSkin.setRank(rank);
            NftPublicBackpackEntity backpack = backpackMap.get(nftSkin.getNftId());
            if (backpack != null) {
                nftSkin.setName(backpack.getName());
                nftSkin.setImage(backpack.getImage());
                nftSkin.setNumber("#" + backpack.getTokenId());
                UcUser ucUser = userMap.get(backpack.getUserId());
                if (ucUser != null) {
                    nftSkin.setUserId(ucUser.getId());
                    nftSkin.setIsOwner(ucUser.getId().equals(currentUser) ? 1 : 0);
                    nftSkin.setCollector(ucUser.getNickname());
                    nftSkin.setPublicAddress(ucUser.getPublicAddress());
                }
            }
            Integer cacheRank = scoreMap.get(nftSkin.getNftId());
            if (cacheRank != null) {
                if (nftSkin.getRank() > cacheRank) {
                    nftSkin.setTrend(2);
                } else if (nftSkin.getRank() < cacheRank) {
                    nftSkin.setTrend(1);
                }
            }
            rank = rank + 1;
        }

        // 设置redis排行榜缓存
        if (!CollectionUtils.isEmpty(respList)) {
            GameRankNftSkinResp resp = new GameRankNftSkinResp();
            resp.setInfos(respList);
            resp.setExpireTime(System.currentTimeMillis() + winRankExpireAfter * 60 * 1000);
            bucket.set(JSONUtil.toJsonStr(resp), winRankExpireAfter, TimeUnit.HOURS);
        }
        return respList;
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

    void buildOrderCondition(Integer sortField, Integer sortType, LambdaQueryWrapper<ServerRoleStatisticsEntity> queryWrap) {
        if (SortTypeEnum.ASC.getCode() == sortType) {
            switch (sortField) {
                case 2:
                    queryWrap.orderByAsc(ServerRoleStatisticsEntity::getFightNum);
                    break;
                case 3:
                    queryWrap.orderByAsc(ServerRoleStatisticsEntity::getWinRate);
                    break;
                case 4:
                    queryWrap.orderByAsc(ServerRoleStatisticsEntity::getWinNum);
                    break;
                case 5:
                    queryWrap.orderByAsc(ServerRoleStatisticsEntity::getSeqWinNum);
                    break;
                case 6:
                    queryWrap.orderByAsc(ServerRoleStatisticsEntity::getMaxKillNum);
                    break;
                case 1:
                default:
                    queryWrap.orderByAsc(ServerRoleStatisticsEntity::getLadderScore);
                    break;
            }
        } else {
            switch (sortField) {
                case 2:
                    queryWrap.orderByDesc(ServerRoleStatisticsEntity::getFightNum);
                    break;
                case 3:
                    queryWrap.orderByDesc(ServerRoleStatisticsEntity::getWinRate);
                    break;
                case 4:
                    queryWrap.orderByDesc(ServerRoleStatisticsEntity::getWinNum);
                    break;
                case 5:
                    queryWrap.orderByDesc(ServerRoleStatisticsEntity::getSeqWinNum);
                    break;
                case 6:
                    queryWrap.orderByDesc(ServerRoleStatisticsEntity::getMaxKillNum);
                    break;
                case 1:
                default:
                    queryWrap.orderByDesc(ServerRoleStatisticsEntity::getLadderScore);
                    break;
            }
        }
    }

    private Map<String, UcUser> getUserMapByAddress(Set<String> publicAddress) {
        Map<String, UcUser> map = new HashMap<>(publicAddress.size());
        try {
            GenericDto<Map<String, UcUser>> result = userCenterFeignClient.mapByPublicAddress(publicAddress);
            map = result.getData();
        } catch (Exception e) {
            log.error("内部批量请求uc获取用户公共地址失败");
        }
        return map;
    }

    private Map<Long, UcUser> getUserMapByIds(Set<Long> ids) {
        Map<Long, UcUser> map = new HashMap<>(ids.size());
        try {
            GenericDto<Map<Long, UcUser>> result = userCenterFeignClient.mapByIds(ids);
            map = result.getData();
        } catch (Exception e) {
            log.error("内部批量请求uc获取用户公共地址失败");
        }
        return map;
    }

}
