package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysRandomCodeDetailResp;
import com.seeds.admin.dto.response.SysRandomCodeResp;
import com.seeds.admin.service.SysRandomCodeService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 随机码管理
 * @author hang.yu
 * @date 2022/11/08
 */
@Slf4j
@Api(tags = "随机码管理")
@RestController
@RequestMapping("/random-code")
public class SysRandomCodeController {

    @Autowired
    private SysRandomCodeService sysRandomCodeService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:randomCode:page")
    public GenericDto<IPage<SysRandomCodeResp>> queryPage(@Valid @RequestBody SysRandomCodePageReq query) {
        return GenericDto.success(sysRandomCodeService.queryPage(query));
    }

    @PostMapping("detail")
    @ApiOperation("明细")
    @RequiredPermission("sys:randomCode:detail")
    public GenericDto<IPage<SysRandomCodeDetailResp>> detail(@Valid @RequestBody SysRandomCodeDetailPageReq query) {
        return GenericDto.success(sysRandomCodeService.detail(query));
    }

    @PostMapping("detail-delete")
    @ApiOperation("明细删除")
    @RequiredPermission("sys:randomCodeDetail:delete")
    public GenericDto<Object> detailDelete(@Valid @RequestBody ListReq req) {
        sysRandomCodeService.detailDelete(req);
        return GenericDto.success(null);
    }

    @PostMapping("generate")
    @ApiOperation("生成")
    @RequiredPermission("sys:randomCode:generate")
    public GenericDto<Object> generate(@Valid @RequestBody SysRandomCodeGenerateReq req) {
        sysRandomCodeService.generate(req);
        return GenericDto.success(null);
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:randomCode:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysRandomCodeModifyReq req) {
        sysRandomCodeService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete/{batchNo}")
    @ApiOperation("删除")
    @RequiredPermission("sys:randomCode:delete")
    public GenericDto<Object> delete(@PathVariable String batchNo) {
        sysRandomCodeService.delete(batchNo);
        return GenericDto.success(null);
    }

    @PostMapping("export/{batchNo}")
    @ApiOperation("导出")
    @RequiredPermission("sys:randomCode:export")
    public GenericDto<String> export(@PathVariable String batchNo) {
        return GenericDto.success(sysRandomCodeService.export(batchNo));
    }


}
