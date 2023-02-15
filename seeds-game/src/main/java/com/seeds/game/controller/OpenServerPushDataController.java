package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.OpenHeroRecordReq;
import com.seeds.game.dto.request.OpenMatchRecordReq;
import com.seeds.game.service.ServerPushDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 游戏服推数据 游戏端调用
 *
 * @author hang.yu
 * @since 2023-02-13
 */
@Api(tags = "游戏服推数据相关接口，游戏端调用")
@RestController
@RequestMapping("/server-push-data")
public class OpenServerPushDataController {

    @Autowired
    private ServerPushDataService serverPushDataService;

    @PostMapping("match-record")
    @ApiOperation("对局记录")
    public GenericDto<Object> matchRecord(@Valid @RequestBody OpenMatchRecordReq matchRecord) {
        serverPushDataService.matchRecord(matchRecord);
        return GenericDto.success(null);
    }

    @GetMapping("hero-record")
    @ApiOperation("英雄记录")
    public GenericDto<Object> heroRecord(@Valid @RequestBody OpenHeroRecordReq heroRecordReq) {
        serverPushDataService.heroRecord(heroRecordReq);
        return GenericDto.success(null);
    }

}
