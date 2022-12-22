package com.seeds.uc.service;

import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GameWinRankResp;

import java.util.List;

/**
 * @author yk
 * @date 2022-12-12
 */
public interface GameRankService {

    /**
     * 获取游戏胜场排行榜
     * @param query 查询条件
     * @return 游戏胜场排行榜
     */
    List<GameWinRankResp.GameWinRank> winInfo(GameWinRankReq query);

}
