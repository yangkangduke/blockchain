package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysGameCommentsAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameCommentsPageReq;
import com.seeds.admin.dto.response.SysGameCommentsResp;
import com.seeds.admin.service.SysGameCommentsService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 游戏评论
 *
 * @author: hewei
 * @date: 2022/8/8
 */
@Slf4j
@Api(tags = "游戏评论管理")
@RestController
@RequestMapping("/game-comments")
public class SysGameCommentsController {

    @Autowired
    private SysGameCommentsService gameCommentsService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:game-comments:page")
    public GenericDto<IPage<SysGameCommentsResp>> queryPage(SysGameCommentsPageReq req) {
        return GenericDto.success(gameCommentsService.queryPage(req));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:game-comments:add")
    public GenericDto<Object> add(@Validated @RequestBody SysGameCommentsAddOrModifyReq req) {
        gameCommentsService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("详情")

    @RequiredPermission("sys:game-comments:detail")
    public GenericDto<SysGameCommentsResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(gameCommentsService.detail(id));
    }

    @PutMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:game-comments:modify")
    public GenericDto<Object> modify(@Validated @RequestBody SysGameCommentsAddOrModifyReq req) {
        gameCommentsService.modify(req);
        return GenericDto.success(null);
    }


    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:game-comments:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req) {
        gameCommentsService.enableOrDisable(req);
        return GenericDto.success(null);
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:game-comments:delete")
    public GenericDto<Object> delete(@Valid @RequestBody ListReq req) {
        gameCommentsService.delete(req);
        return GenericDto.success(null);
    }
}
