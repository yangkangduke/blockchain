package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysMerchantAddReq;
import com.seeds.admin.dto.request.SysMerchantModifyReq;
import com.seeds.admin.dto.request.SysMerchantPageReq;
import com.seeds.admin.dto.request.SysMerchantUserAddReq;
import com.seeds.admin.dto.response.SysMerchantResp;
import com.seeds.admin.entity.SysMerchantEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.service.SysMerchantService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 商家管理
 * @author hang.yu
 * @date 2022/7/14
 */
@Slf4j
@Api("商家管理")
@RestController
@RequestMapping("/merchant")
public class SysMerchantController {

    @Autowired
    private SysMerchantService sysMerchantService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:merchant:page")
    public GenericDto<IPage<SysMerchantResp>> queryPage(@Valid @RequestBody SysMerchantPageReq query) {
        return GenericDto.success(sysMerchantService.queryPage(query));
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:merchant:list")
    public GenericDto<List<SysMerchantResp>> list() {
        return GenericDto.success(sysMerchantService.queryList());
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:merchant:add")
    public GenericDto<Object> add(@Valid @RequestBody SysMerchantAddReq req) {
        SysMerchantEntity merchant = sysMerchantService.queryByUrl(req.getUrl());
        if (merchant != null) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_50001_MERCHANT_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_50001_MERCHANT_ALREADY_EXIST.getCode(), null);
        }
        sysMerchantService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:merchant:detail")
    public GenericDto<SysMerchantResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysMerchantService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:merchant:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysMerchantModifyReq req) {
        SysMerchantEntity merchant = sysMerchantService.queryByUrl(req.getUrl());
        if (merchant != null && !Objects.equals(merchant.getId(), req.getId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_50001_MERCHANT_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_50001_MERCHANT_ALREADY_EXIST.getCode(), null);
        }
        sysMerchantService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:merchant:delete")
    public GenericDto<Object> delete(@RequestBody ListReq req) {
        sysMerchantService.batchDelete(req);
        return GenericDto.success(null);
    }

    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:merchant:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req) {
        sysMerchantService.enableOrDisable(req);
        return GenericDto.success(null);
    }

    @PostMapping("addGame/{merchantId}")
    @ApiOperation("添加游戏")
    @RequiredPermission("sys:merchant:addGame")
    public GenericDto<Object> addGame(@Valid @RequestBody ListReq req, @PathVariable("merchantId") Long merchantId) {
        sysMerchantService.addGame(req, merchantId);
        return GenericDto.success(null);
    }

    @PostMapping("deleteGame/{merchantId}")
    @ApiOperation("删除游戏")
    @RequiredPermission("sys:merchant:deleteGame")
    public GenericDto<Object> deleteGame(@Valid @RequestBody ListReq req, @PathVariable("merchantId") Long merchantId) {
        sysMerchantService.deleteGame(req, merchantId);
        return GenericDto.success(null);
    }

    @PostMapping("addUser/{merchantId}")
    @ApiOperation("添加用户")
    @RequiredPermission("sys:merchant:addUser")
    public GenericDto<Object> addUser(@Valid @RequestBody SysMerchantUserAddReq req, @PathVariable("merchantId") Long merchantId) {
        sysMerchantService.addUser(req, merchantId);
        return GenericDto.success(null);
    }

    @PostMapping("deleteUser/{merchantId}")
    @ApiOperation("删除用户")
    @RequiredPermission("sys:merchant:deleteUser")
    public GenericDto<Object> deleteUser(@Valid @RequestBody ListReq req, @PathVariable("merchantId") Long merchantId) {
        sysMerchantService.deleteUser(req, merchantId);
        return GenericDto.success(null);
    }


}
