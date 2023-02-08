package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.response.ServerRoleHeroStatisticsResp;
import com.seeds.game.dto.response.ServerRoleStatisticsResp;
import com.seeds.game.service.ServerStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 游戏服统计 web端调用
 *
 * @author hang.yu
 * @since 2023-02-08
 */
@Api(tags = "游戏服统计相关接口，web调用")
@RestController
@RequestMapping("/web/server-statistics")
public class ServerStatisticsController {

    @Autowired
    private ServerStatisticsService serverStatisticsService;

    @GetMapping("role-data/{region}/{server}")
    @ApiOperation("角色数据统计")
    public GenericDto<ServerRoleStatisticsResp> roleData(@PathVariable Integer region, @PathVariable Integer server) {
        return GenericDto.success(serverStatisticsService.roleData(UserContext.getCurrentUserId(), region, server));
    }

    @GetMapping("role-hero-data/{region}/{server}")
    @ApiOperation("角色的英雄数据统计")
    public GenericDto<List<ServerRoleHeroStatisticsResp>> roleHeroData(@PathVariable Integer region, @PathVariable Integer server) {
        return GenericDto.success(serverStatisticsService.roleHeroData(UserContext.getCurrentUserId(), region, server));
    }

}
