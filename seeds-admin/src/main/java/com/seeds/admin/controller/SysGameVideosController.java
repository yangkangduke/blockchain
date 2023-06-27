package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.SysGameVideoAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameVideosReq;
import com.seeds.admin.dto.response.SysGameVideosResp;
import com.seeds.admin.service.ISysGameVideosService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 游戏视频管理 前端控制器
 * </p>
 *
 * @author hewei
 * @since 2023-06-25
 */
@Api(tags = "游戏视频管理")
@RestController
@RequestMapping("/videos")
public class SysGameVideosController {

    @Resource
    private ISysGameVideosService gameVideosService;

    @PostMapping("page")
    @ApiOperation("分页")
    public GenericDto<IPage<SysGameVideosResp>> queryPage(@RequestBody SysGameVideosReq req) {
        return GenericDto.success(gameVideosService.queryPage(req));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    public GenericDto<Object> add(@RequestBody SysGameVideoAddOrModifyReq req) {
        gameVideosService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    public GenericDto<SysGameVideosResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(gameVideosService.detail(id));
    }

    @PutMapping("modify")
    @ApiOperation("编辑")
    public GenericDto<Object> modify(@RequestBody SysGameVideoAddOrModifyReq req) {
        gameVideosService.modify(req);
        return GenericDto.success(null);
    }

    @PutMapping("onShelves")
    @ApiOperation("上下架")
    public GenericDto<Object> onShelves(@RequestBody SysGameVideoAddOrModifyReq req) {
        gameVideosService.onShelves(req);
        return GenericDto.success(null);
    }

    @PutMapping("top")
    @ApiOperation("置顶/取消置顶")
    public GenericDto<Object> top(@RequestBody SysGameVideoAddOrModifyReq req) {
        gameVideosService.top(req);
        return GenericDto.success(null);
    }


}
