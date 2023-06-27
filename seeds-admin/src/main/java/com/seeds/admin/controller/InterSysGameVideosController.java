package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.SysGameVideosReq;
import com.seeds.admin.dto.response.SysGameVideosResp;
import com.seeds.admin.service.ISysGameVideosService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 游戏视频管理 前端控制器
 * </p>
 *
 * @author hewei
 * @since 2023-06-25
 */
@RestController
@RequestMapping("/internal-videos")
public class InterSysGameVideosController {

    @Autowired
    private ISysGameVideosService gameVideosService;

    @PostMapping("page")
    @ApiOperation("分页")
    @Inner
    public GenericDto<IPage<SysGameVideosResp>> queryPage(@RequestBody SysGameVideosReq req) {
        return GenericDto.success(gameVideosService.queryPageForUc(req));
    }

    @PostMapping("top-videos")
    @ApiOperation("获取置顶视频")
    @Inner
    public GenericDto<List<SysGameVideosResp>> getTopVideos() {
        return GenericDto.success(gameVideosService.getTopVideos());
    }
}
