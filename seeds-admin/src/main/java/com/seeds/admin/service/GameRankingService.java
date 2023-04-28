package com.seeds.admin.service;


import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GameWinRankResp;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 游戏排行榜管理
 * @author hang.yu
 * @date 2022/04/26
 */
public interface GameRankingService {

    /**
     * 获取游戏胜场排行榜
     * @param query 查询条件
     * @return 游戏胜场排行榜
     */
    List<GameWinRankResp.GameWinRank> queryList(GameWinRankReq query);

    /**
     * 导出游戏胜场排行榜
     * @param records 排行榜记录
     * @param response 响应
     */
    void export(List<GameWinRankResp.GameWinRank> records, HttpServletResponse response);

}
