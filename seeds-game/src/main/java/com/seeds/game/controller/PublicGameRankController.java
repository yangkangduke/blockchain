package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.GameRankStatisticPageReq;
import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GameRankStatisticResp;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.service.GameRankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * <p>
 * 游戏排行榜 前端控制器
 * </p>
 *
 * @author hang.yu
 * @date 2022-12-12
 */
@RestController
@RequestMapping("/public/web/game-rank")
@Api(tags = "公共游戏排行榜")
public class PublicGameRankController {

    @Autowired
    private GameRankService gameRankService;

    @PostMapping("/win-info")
    @ApiOperation(value = "胜场数据", notes = "胜场数据")
    public GenericDto<List<GameWinRankResp.GameWinRank>> winInfo(@Valid @RequestBody GameWinRankReq query) {
        return GenericDto.success(gameRankService.winInfo(query, true));
    }

    @PostMapping("/statistic-page")
    @ApiOperation(value = "统计分页", notes = "统计分页")
    public GenericDto<IPage<GameRankStatisticResp>> statisticPage(@Valid @RequestBody GameRankStatisticPageReq query) {
        return GenericDto.success(gameRankService.statisticPage(query));
    }

}
