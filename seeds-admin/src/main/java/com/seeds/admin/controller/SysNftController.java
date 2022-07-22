package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.service.SysNftService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * NFT管理
 * @author hang.yu
 * @date 2022/7/22
 */
@Slf4j
@Api("NFT管理")
@RestController
@RequestMapping("/nft")
public class SysNftController extends AdminBaseController {

    @Autowired
    private SysNftService sysNftService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:nft:page")
    public GenericDto<IPage<SysNftResp>> queryPage(@Valid @RequestBody SysNftPageReq query) {
        return GenericDto.success(sysNftService.queryPage(query));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:nft:add")
    public GenericDto<Object> add(@Valid @RequestBody SysNftAddReq req) {
        sysNftService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:nft:detail")
    public GenericDto<SysNftDetailResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysNftService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:nft:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysNftModifyReq req) {
        sysNftService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:nft:delete")
    public GenericDto<Object> delete(@RequestBody ListReq req) {
        sysNftService.batchDelete(req);
        return GenericDto.success(null);
    }

    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:nft:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req) {
        sysNftService.enableOrDisable(req);
        return GenericDto.success(null);
    }

}