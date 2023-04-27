package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysKolResp;
import com.seeds.admin.service.SysKolService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * KOL管理
 * @author hang.yu
 * @date 2023/4/26
 */
@Slf4j
@Api(tags = "KOL管理")
@RestController
@RequestMapping("/kol")
public class SysKolController {

    @Autowired
    private SysKolService sysKolService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:kol:page")
    public GenericDto<IPage<SysKolResp>> queryPage(@Valid @RequestBody SysKolPageReq query) {
        return GenericDto.success(sysKolService.queryPage(query));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:kol:add")
    public GenericDto<Object> add(@Valid @RequestBody SysKolAddReq req) {
        sysKolService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:kol:detail")
    public GenericDto<SysKolResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysKolService.detail(id));
    }

    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:kol:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req) {
        sysKolService.enableOrDisable(req);
        return GenericDto.success(null);
    }

    @GetMapping("check/{email}")
    @ApiOperation("检查")
    @RequiredPermission("sys:kol:check")
    public GenericDto<String> check(@PathVariable String email) {
        return GenericDto.success(sysKolService.check(email));
    }

}
