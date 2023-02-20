package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.response.ServerRegionResp;
import com.seeds.game.service.IServerRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 游戏区服 web端调用
 *
 * @author hang.yu
 * @since 2023-02-16
 */
@Api(tags = "游戏区服相关接口，web调用")
@RestController
@RequestMapping("/web/server-region")
public class ServerRegionController {

    @Autowired
    private IServerRegionService serverRegionService;

    @PostMapping("list")
    @ApiOperation("获取游戏区服")
    public GenericDto<List<ServerRegionResp>> queryList() {
        return GenericDto.success(serverRegionService.queryList());
    }

}
