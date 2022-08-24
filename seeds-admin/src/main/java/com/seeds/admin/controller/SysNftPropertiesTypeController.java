package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.DataFilter;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftPropertiesTypeBriefResp;
import com.seeds.admin.dto.response.SysNftPropertiesTypeResp;
import com.seeds.admin.entity.SysNftPropertiesTypeEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.service.SysNftPropertiesTypeService;
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
 * NFT属性类别管理
 * @author hang.yu
 * @date 2022/7/22
 */
@Slf4j
@Api(tags = "NFT属性类别管理")
@RestController
@RequestMapping("/nft-properties-type")
public class SysNftPropertiesTypeController {

    @Autowired
    private SysNftPropertiesTypeService sysNftPropertiesTypeService;

    @PostMapping("page")
    @ApiOperation("分页")
    @DataFilter
    @RequiredPermission("sys:nftPT:page")
    public GenericDto<IPage<SysNftPropertiesTypeResp>> queryPage(@Valid @RequestBody SysNftPropertiesTypePageReq query) {
        return GenericDto.success(sysNftPropertiesTypeService.queryPage(query));
    }

    @PostMapping("dropdown-list")
    @ApiOperation("下拉列表")
    public GenericDto<List<SysNftPropertiesTypeBriefResp>> dropdownList() {
        return GenericDto.success(sysNftPropertiesTypeService.dropdownList());
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:nftPT:add")
    public GenericDto<Object> add(@Valid @RequestBody SysNftPropertiesTypeAddReq req) {
        // 查重
        SysNftPropertiesTypeEntity nft = sysNftPropertiesTypeService.queryByCode(req.getCode());
        if (nft != null) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_40005_NFT_PROPERTIES_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_40005_NFT_PROPERTIES_TYPE_ALREADY_EXIST.getCode(), null);
        }
        sysNftPropertiesTypeService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:nftPT:detail")
    public GenericDto<SysNftPropertiesTypeResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysNftPropertiesTypeService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:nftPT:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysNftTypeModifyReq req) {
        // 查重
        SysNftPropertiesTypeEntity nft = sysNftPropertiesTypeService.queryByCode(req.getCode());
        if (nft != null && !Objects.equals(nft.getId(), req.getId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_40005_NFT_PROPERTIES_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_40005_NFT_PROPERTIES_TYPE_ALREADY_EXIST.getCode(), null);
        }
        sysNftPropertiesTypeService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:nftPT:delete")
    public GenericDto<Object> delete(@RequestBody ListReq req) {
        sysNftPropertiesTypeService.batchDelete(req);
        return GenericDto.success(null);
    }

}
