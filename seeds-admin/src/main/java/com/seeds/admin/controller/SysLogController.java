package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.SysLogPageReq;
import com.seeds.admin.dto.response.SysLogResp;
import com.seeds.admin.service.SysLogService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 游戏评论
 *
 * @author: hewei
 * @date: 2022/8/8
 */
@Slf4j
@Api(tags = "系统操作日志管理")
@RestController
@RequestMapping("/log")
public class SysLogController {

    @Autowired
    private SysLogService logService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:log:page")
    public GenericDto<IPage<SysLogResp>> queryPage(@RequestBody SysLogPageReq req) {
        return GenericDto.success(logService.queryPage(req));
    }
}
