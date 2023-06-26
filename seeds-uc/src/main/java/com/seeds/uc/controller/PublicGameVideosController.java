package com.seeds.uc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGameVideosReq;
import com.seeds.admin.dto.response.SysGameVideosResp;
import com.seeds.admin.feign.RemoteGameVideosService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: hewei
 * @date 2023/6/25
 */
@RestController
@Slf4j
@Api(tags = "游戏视频")
public class PublicGameVideosController {
    @Resource
    private RemoteGameVideosService remoteGameVideosService;

    @PostMapping("/public/game/top-videos")
    @ApiOperation("置顶视频")
    public GenericDto<List<SysGameVideosResp>> getTopVideos() {
        return remoteGameVideosService.getTopVideos();
    }

    @PostMapping("/public/game/videos-page")
    @ApiOperation("分页列表")
    public GenericDto<Page<SysGameVideosResp>> queryPage(@RequestBody SysGameVideosReq req) {
        return remoteGameVideosService.queryPage(req);
    }
}
