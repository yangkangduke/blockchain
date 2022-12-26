package com.seeds.admin.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.admin.dto.response.ProfileInfoResp;
import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.entity.SysGameApiEntity;
import com.seeds.admin.entity.SysGameEntity;
import com.seeds.admin.entity.SysGameTypeEntity;
import com.seeds.admin.enums.SortTypeEnum;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mapper.SysGameMapper;
import com.seeds.admin.service.*;
import com.seeds.admin.utils.HashUtil;
import com.seeds.common.enums.ApiType;
import com.seeds.uc.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统游戏
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Slf4j
@Service
public class SysGameServiceImpl extends ServiceImpl<SysGameMapper, SysGameEntity> implements SysGameService {

    @Autowired
    private SysMerchantGameService sysMerchantGameService;

    @Autowired
    private SysSequenceNoService sysSequenceNoService;

    @Autowired
    private SysGameTypeService sysGameTypeService;

    @Autowired
    private SysGameApiService sysGameApiService;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysGameMapper sysGameMapper;

    @Override
    public IPage<SysGameResp> queryPage(SysGamePageReq query) {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .eq(query.getStatus() != null, SysGameEntity::getStatus, query.getStatus())
                .eq(query.getTypeId() != null, SysGameEntity::getTypeId, query.getTypeId())
                .likeRight(StringUtils.isNotBlank(query.getName()), SysGameEntity::getName, query.getName());
        buildOrderBy(query, queryWrap);
        Page<SysGameEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysGameEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> typeIds = records.stream().map(SysGameEntity::getTypeId).collect(Collectors.toSet());
        Map<Long, SysGameTypeEntity> nftGameMap = sysGameTypeService.queryMapByIds(typeIds);
        return page.convert(p -> {
            SysGameResp resp = new SysGameResp();
            BeanUtils.copyProperties(p, resp);
            SysGameTypeEntity gameType = nftGameMap.get(p.getTypeId());
            if (gameType != null) {
                resp.setTypeId(gameType.getId());
                resp.setTypeName(gameType.getName());
            }
            return resp;
        });
    }

    @Override
    public List<SysGameResp> select(Long merchantId) {
        List<SysGameEntity> games = new ArrayList<>();
        if (merchantId != null) {
            // 查询商家下的游戏
            Set<Long> gameIds = sysMerchantGameService.queryGameIdByMerchantId(merchantId);
            if (!CollectionUtils.isEmpty(gameIds)) {
                games = listByIds(gameIds);
            }
        } else {
            games = list();
        }
        List<SysGameResp> respList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(games)) {
            games.forEach(p -> {
                SysGameResp resp = new SysGameResp();
                BeanUtils.copyProperties(p, resp);
                respList.add(resp);
            });
        }
        return respList;
    }

    @Override
    public SysGameEntity queryById(Long id) {
        return sysGameMapper.queryById(id);
    }

    @Override
    public void add(SysGameAddReq req) {
        Long gameAccessNo = sysSequenceNoService.generateGameAccessNo();
        SysGameEntity game = new SysGameEntity();
        game.setAccessKey(RandomUtil.randomString(7) + gameAccessNo);
        game.setSecretKey(gameAccessNo + HashUtil.MD5(RandomUtil.randomString(8).toUpperCase()));
        BeanUtils.copyProperties(req, game);
        save(game);
    }

    @Override
    public SysGameResp detail(Long id) {
        SysGameEntity sysGame = getById(id);
        SysGameResp resp = new SysGameResp();
        if (sysGame != null) {
            BeanUtils.copyProperties(sysGame, resp);
            // 游戏类别信息
            SysGameTypeEntity sysGameType = sysGameTypeService.queryById(sysGame.getTypeId());
            if (sysGameType != null) {
                resp.setTypeId(sysGameType.getId());
                resp.setTypeName(sysGameType.getName());
            }
        }
        return resp;
    }

    @Override
    public void modify(SysGameModifyReq req) {
        SysGameEntity sysGame = new SysGameEntity();
        BeanUtils.copyProperties(req, sysGame);
        updateById(sysGame);
    }

    @Override
    public void batchDelete(ListReq req) {
        // 删除游戏
        removeBatchByIds(req.getIds());
        // 删除商家和游戏的关联
        sysMerchantGameService.deleteByGameIds(req.getIds());
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysGameEntity sysGame = new SysGameEntity();
            sysGame.setId(p.getId());
            sysGame.setStatus(p.getStatus());
            updateById(sysGame);
        });
    }

    @Override
    public Map<Long, String> queryMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        // 被删除的游戏也需要展示
        List<SysGameEntity> sysGames = sysGameMapper.queryListByIds(ids);
        if (CollectionUtils.isEmpty(sysGames)) {
            return Collections.emptyMap();
        }
        return sysGames.stream().collect(Collectors.toMap(SysGameEntity::getId, SysGameEntity::getName));
    }

    @Override
    public SysGameEntity queryByOfficialUrlOrName(String officialUrl, String name) {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .eq(SysGameEntity::getOfficialUrl, officialUrl).or().eq(SysGameEntity::getName, name);
        return getOne(queryWrap);
    }

    @Override
    public List<SysGameBriefResp> dropdownList() {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .eq(SysGameEntity::getStatus, WhetherEnum.YES.value());
        List<SysGameEntity> records = list(queryWrap);
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }
        List<SysGameBriefResp> respList = new ArrayList<>();
        records.forEach(p -> {
            SysGameBriefResp resp = new SysGameBriefResp();
            BeanUtils.copyProperties(p, resp);
            respList.add(resp);
        });
        return respList;
    }

    @Override
    public void ucCollection(Long id) {
        LambdaUpdateWrapper<SysGameEntity> queryWrap = new UpdateWrapper<SysGameEntity>().lambda()
                .setSql("`collections`=`collections`+1")
                .eq(SysGameEntity::getId, id);
        update(queryWrap);
    }

    @Override
    public String querySecretKey(String accessKey) {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .eq(SysGameEntity::getAccessKey, accessKey);
        SysGameEntity game = getOne(queryWrap);
        if (game == null) {
            return null;
        }
        return game.getSecretKey();
    }

    @Override
    public List<GameWinRankResp.GameWinRank> winRankInfo(GameWinRankReq query) {
        // 通知游戏方NFT创建结果
        SysGameApiEntity gameApi = sysGameApiService.queryByGameAndType(query.getGameId(), ApiType.PLAYER_WIN_RANK.getCode());
        List<String> rankUrls = new ArrayList<>();
        if (gameApi.getBaseUrl().contains("|")) {
            rankUrls = Arrays.stream(gameApi.getBaseUrl().split("\\|")).map(p -> p + gameApi.getApi()).collect(Collectors.toList());
        } else {
            rankUrls.add(gameApi.getBaseUrl() + gameApi.getApi());
        }
        List<GameWinRankResp.GameWinRank> rankList = new ArrayList<>();
        for (String rankUrl : rankUrls) {
            String params = String.format("startRow=%s&endRow=%s", query.getStartRow(), query.getEndRow() * 2);
            rankUrl = rankUrl + "?" + params;
            log.info("开始请求游戏胜场排行榜数据， params:{}", params);
            HttpResponse response = HttpRequest.get(rankUrl)
                    .timeout(5 * 60 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            String body = response.body();
            log.info("请求游戏胜场排行榜数据返回，result:{}", body);
            if (!response.isOk()) {
                log.error("Failed to get the win list from game");
                throw new GenericException("Failed to get the win list");
            }
            GameWinRankResp resp = JSONUtil.toBean(body, GameWinRankResp.class);
            if (!resp.getRet().equalsIgnoreCase("OK")) {
                log.error("Get the win list from game failed");
                throw new GenericException("Get the win list from game failed");
            }
            rankList.addAll(resp.getInfos());
        }
        if (CollectionUtils.isEmpty(rankList)) {
            return Collections.emptyList();
        }
        Map<Long, GameWinRankResp.GameWinRank> rankMap = new HashMap<>(rankList.size());
        for (GameWinRankResp.GameWinRank rank : rankList) {
            Long key = rank.getAccID();
            GameWinRankResp.GameWinRank mapRank = rankMap.get(key);
            if (mapRank == null) {
                rankMap.put(key, rank);
            } else {
                // 总场数
                mapRank.setFightNum(mapRank.getFightNum() + rank.getFightNum());
                // 最高连胜场数
                mapRank.setMaxSeqWin(mapRank.getMaxSeqWin() > rank.getMaxSeqWin() ? mapRank.getMaxSeqWin() : rank.getMaxSeqWin());
                // 总积分
                mapRank.setScore(mapRank.getScore() + rank.getScore());
                // 总胜利场数
                mapRank.setWinNum(mapRank.getWinNum() + rank.getWinNum());
                // 头像url
                mapRank.setPortraitUrl(sysFileService.getFileUrl("game/" + gameApi.getDesc() + mapRank.getPortraitId() + ".jpg"));
                rankMap.put(key, mapRank);
            }
        }
        return rankMap.values().stream()
                .sorted(Comparator.comparing(GameWinRankResp.GameWinRank::getScore).reversed())
                .limit(query.getEndRow()).collect(Collectors.toList());
    }

    @Override
    public ProfileInfoResp profileInfo(Long gameId, String email) {
        // 通知游戏方NFT创建结果
        List<String> rankUrls = sysGameApiService.queryUrlByGameAndType(gameId, ApiType.PROFILE_INFO.getCode());
        ProfileInfoResp profileInfo = null;
        List<ProfileInfoResp.GameHeroRecord> heroRecords = new ArrayList<>();
        for (String rankUrl : rankUrls) {
            String params = String.format("accName=%s", email);
            rankUrl = rankUrl + "?" + params;
            log.info("开始请求个人游戏概括信息数据，params:{}", params);
            HttpResponse response = HttpRequest.get(rankUrl)
                    .timeout(5 * 60 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            String body = response.body();
            log.info("请求个人游戏概括信息数据返回，result:{}", body);
            if (!response.isOk()) {
                log.error("Failed to get the profile info from game");
                throw new GenericException("Failed to get the profile info");
            }
            ProfileInfoResp resp = JSONUtil.toBean(body, ProfileInfoResp.class);
            if (!resp.getRet().equalsIgnoreCase("OK")) {
                log.error("Get the profile info from game failed");
                throw new GenericException("Get the profile info from game failed");
            }
            if (profileInfo == null) {
                profileInfo = resp;
            } else {
                // 胜利总场次
                profileInfo.setWinNum(profileInfo.getWinNum() + resp.getWinNum());
                // 最大连胜场次
                profileInfo.setMaxSeqWin(profileInfo.getMaxSeqWin() > resp.getMaxSeqWin() ? profileInfo.getMaxSeqWin() : resp.getMaxSeqWin());
                // 战斗总场次
                profileInfo.setFightNum(profileInfo.getFightNum() + resp.getFightNum());
                // 总积分
                profileInfo.setScore(profileInfo.getScore() + resp.getScore());
            }
            heroRecords.addAll(resp.getHeroRecord());
        }
        // 多个服都没有数据
        if (profileInfo == null) {
            return null;
        }
        Map<Long, ProfileInfoResp.GameHeroRecord> recordMap = new HashMap<>(heroRecords.size());
        if (!CollectionUtils.isEmpty(heroRecords)) {
            for (ProfileInfoResp.GameHeroRecord record : heroRecords) {
                Long key = record.getId();
                ProfileInfoResp.GameHeroRecord mapRecord = recordMap.get(key);
                if (mapRecord == null) {
                    recordMap.put(key, record);
                } else {
                    // 胜利总数
                    mapRecord.setTw(mapRecord.getTw() + record.getTw());
                    // 最大连杀数
                    mapRecord.setMsk(mapRecord.getMsk() > record.getMsk() ? mapRecord.getMsk() : record.getMsk());
                    // 最多救援
                    mapRecord.setMs(mapRecord.getMs() > record.getMs() ? mapRecord.getMs() : record.getMs());
                    // 英雄使用次数
                    mapRecord.setNum(mapRecord.getNum() + record.getNum());
                    // 3S次数
                    mapRecord.setTsss(mapRecord.getTsss() + record.getTsss());
                    // 最大得分
                    mapRecord.setMts(mapRecord.getMts() > record.getMts() ? mapRecord.getMts() : record.getMts());
                    // 总收集
                    mapRecord.setTc(mapRecord.getTc() + record.getTc());
                    // 前几名总次数
                    mapRecord.setTtn(mapRecord.getTtn() + record.getTtn());
                    // 最长存活时间
                    mapRecord.setMst(mapRecord.getMst() > record.getMst() ? mapRecord.getMst() : record.getMst());
                    // 死亡总数
                    mapRecord.setTd(mapRecord.getTd() + record.getTd());
                    // 总存活时间
                    mapRecord.setTst(mapRecord.getTst() + record.getTst());
                    // 最多清兵数
                    mapRecord.setMks(mapRecord.getMks() > record.getMks() ? mapRecord.getMks() : record.getMks());
                    // 最大收集
                    mapRecord.setMc(mapRecord.getMc() > record.getMc() ? mapRecord.getMc() : record.getMc());
                    // 击杀总数
                    mapRecord.setTk(mapRecord.getTk() + record.getTk());
                    // 总清兵数
                    mapRecord.setTks(mapRecord.getTks() + record.getTks());
                    // 总救援
                    mapRecord.setTs(mapRecord.getTs() + record.getTs());
                    // 英雄最大连胜次数
                    mapRecord.setMsw(mapRecord.getMsw() > record.getMsw() ? mapRecord.getMsw() : record.getMsw());
                    // 英雄连胜次数
                    mapRecord.setSw(mapRecord.getSw() > record.getSw() ? mapRecord.getSw() : record.getSw());
                    // 英雄胜率（胜场数/总场数）
                    mapRecord.setWinRate(new BigDecimal(mapRecord.getTw() / mapRecord.getNum()).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) + "%");
                    recordMap.put(key, mapRecord);
                }
            }
        }
        profileInfo.setHeroRecord(new ArrayList<>(recordMap.values()));
        return profileInfo;
    }

    private void buildOrderBy(SysGamePageReq query, LambdaQueryWrapper<SysGameEntity> queryWrap) {
        if (query.getSortType() != null) {
            int descFlag = query.getDescFlag() == null ? 0 : query.getDescFlag();
            if (WhetherEnum.YES.value() == descFlag) {
                queryWrap.orderByDesc(getOrderType(query.getSortType()));
            } else {
                queryWrap.orderByAsc(getOrderType(query.getSortType()));
            }
        } else {
            queryWrap.orderByDesc(SysGameEntity::getCreatedAt);
        }
    }

    private SFunction<SysGameEntity, ?> getOrderType(Integer sortType) {
        SortTypeEnum type = SortTypeEnum.from(sortType);
        switch (type) {
            case RANK:
                return SysGameEntity::getRank;
            case PRICE:
                return SysGameEntity::getPrice;
            default:
                return SysGameEntity::getCreatedAt;
        }
    }
}

