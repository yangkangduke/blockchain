package com.seeds.admin.controller;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.admin.service.GameRankingService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 游戏排行榜管理
 * @author hang.yu
 * @date 2022/04/26
 */
@Slf4j
@Api(tags = "游戏排行榜管理")
@RestController
@RequestMapping("/game-rank")
public class GameRankingController {

    @Autowired
    private GameRankingService gameRankingService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:gameRank:list")
    public GenericDto<List<GameWinRankResp.GameWinRank>> queryList(@Valid @RequestBody GameWinRankReq query) {
        return GenericDto.success(gameRankingService.queryList(query));
    }

    @PostMapping("export")
    @ApiOperation("导出")
    @RequiredPermission("sys:gameRank:export")
    public void export(@Valid @RequestBody GameWinRankReq query, HttpServletResponse response) {
        gameRankingService.export(gameRankingService.queryList(query), response);
    }

}
