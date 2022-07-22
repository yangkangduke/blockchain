package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.SysNftAddReq;
import com.seeds.admin.dto.request.SysNftPageReq;
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
        //sysNftService.add(req);
        return GenericDto.success(null);
    }

}
