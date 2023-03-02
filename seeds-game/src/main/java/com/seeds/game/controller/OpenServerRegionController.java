package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.OpenServerRegionCreateUpdateReq;
import com.seeds.game.service.IServerRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 游戏区服 游戏端调用
 *
 * @author hewei
 * @since 2023-03-02
 */
@Api(tags = "游戏区服相关接口，游戏端调用")
@RestController
@RequestMapping("/web/server-region")
public class OpenServerRegionController {

    @Autowired
    private IServerRegionService serverRegionService;

    @PostMapping("createOrUpdate")
    @ApiOperation("新增/编辑角色信息")
    public GenericDto<Object> createOrUpdate(@RequestBody @Valid OpenServerRegionCreateUpdateReq req) {
        serverRegionService.createOrUpdate(req);
        return GenericDto.success(null);
    }

}
