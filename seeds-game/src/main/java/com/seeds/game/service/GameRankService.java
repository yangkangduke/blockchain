package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.GameRankNftPageReq;
import com.seeds.admin.dto.request.GameRankStatisticPageReq;
import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.*;

import java.util.List;

/**
 * @author yk
 * @date 2022-12-12
 */
public interface GameRankService {

    /**
     * 获取游戏胜场排行榜
     * @param query 查询条件
     * @param useCache 是否使用缓存
     * @return 游戏胜场排行榜
     */
    List<GameWinRankResp.GameWinRank> winInfo(GameWinRankReq query, Boolean useCache);

    /**
     * 获取游戏胜场排行统计分页
     * @param query 查询条件
     * @return 游戏胜场排行统计
     */
    IPage<GameRankStatisticResp> statisticPage(GameRankStatisticPageReq query);

    /**
     * 获取游戏装备排行分页
     * @param query 查询条件
     * @return 游戏装备排行
     */
    IPage<GameRankEquipResp> equipPage(GameRankNftPageReq query);

    /**
     * 获取游戏道具排行分页
     * @param query 查询条件
     * @return 游戏道具排行
     */
    IPage<GameRankItemResp> itemPage(GameRankNftPageReq query);

    /**
     * 获取游戏英雄排行分页
     * @param query 查询条件
     * @return 游戏英雄排行
     */
    IPage<GameRankHeroResp> heroPage(GameRankNftPageReq query);

}
