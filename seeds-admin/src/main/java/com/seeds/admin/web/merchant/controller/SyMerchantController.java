package com.seeds.admin.web.merchant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.merchant.request.SysMerchantAddReq;
import com.seeds.admin.dto.merchant.request.SysMerchantModifyReq;
import com.seeds.admin.dto.merchant.request.SysMerchantPageReq;
import com.seeds.admin.dto.merchant.response.SysMerchantResp;
import com.seeds.admin.web.common.controller.AdminBaseController;
import com.seeds.admin.web.merchant.service.SysMerchantService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author hang.yu
 * @date 2022/7/14
 */
@Slf4j
@RestController
@RequestMapping("/merchant")
public class SyMerchantController extends AdminBaseController {

    @Autowired
    private SysMerchantService sysMerchantService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:merchant:page")
    public GenericDto<IPage<SysMerchantResp>> queryPage(@Valid @RequestBody SysMerchantPageReq query){
        return GenericDto.success(sysMerchantService.queryPage(query));
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:merchant:list")
    public GenericDto<List<SysMerchantResp>> list(){
        return GenericDto.success(sysMerchantService.queryList());
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:merchant:add")
    public GenericDto<Object> add(@Valid @RequestBody SysMerchantAddReq req){
        sysMerchantService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:merchant:detail")
    public GenericDto<SysMerchantResp> detail(@PathVariable("id") Long id){
        return GenericDto.success(sysMerchantService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:merchant:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysMerchantModifyReq req){
        sysMerchantService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete/{id}")
    @ApiOperation("删除")
    @RequiredPermission("sys:merchant:delete")
    public GenericDto<Object> delete(@PathVariable("id") Long id){
        sysMerchantService.delete(id);
        return GenericDto.success(null);
    }

}
