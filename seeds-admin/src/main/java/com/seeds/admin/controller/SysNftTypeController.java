package com.seeds.admin.controller;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysNftTypeAddReq;
import com.seeds.admin.dto.request.SysNftTypeModifyReq;
import com.seeds.admin.dto.response.SysNftTypeResp;
import com.seeds.admin.entity.SysNftTypeEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.service.SysNftTypeService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * NFT类别管理
 * @author hang.yu
 * @date 2022/7/21
 */
@Slf4j
@Api(tags = "NFT类别管理")
@RestController
@RequestMapping("/nft-type")
public class SysNftTypeController {

    @Autowired
    private SysNftTypeService sysNftTypeService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:nftType:list")
    public GenericDto<List<SysNftTypeResp>> list(@RequestParam(required = false) String name) {
        return GenericDto.success(sysNftTypeService.queryRespList(name));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:nftType:add")
    public GenericDto<Object> add(@Valid @RequestBody SysNftTypeAddReq req) {
        // 查重
        SysNftTypeEntity nftType = sysNftTypeService.queryByTypeCode(req.getCode());
        if (nftType != null) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_40001_NFT_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_40001_NFT_TYPE_ALREADY_EXIST.getCode(), null);
        }
        sysNftTypeService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:nftType:detail")
    public GenericDto<SysNftTypeResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysNftTypeService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:nftType:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysNftTypeModifyReq req) {
        // 查重
        SysNftTypeEntity nftType = sysNftTypeService.queryByTypeCode(req.getCode());
        if (nftType != null && !Objects.equals(nftType.getId(), req.getId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_40001_NFT_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_40001_NFT_TYPE_ALREADY_EXIST.getCode(), null);
        }
        // 上级类别不能为自身
        if (req.getCode().equals(req.getParentCode())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_40002_NFT_TYPE_PARENT_ITSELF.getDescEn(), AdminErrorCodeEnum.ERR_40002_NFT_TYPE_PARENT_ITSELF.getCode(), null);
        }
        sysNftTypeService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:nftType:delete")
    public GenericDto<Object> delete(@Valid @RequestBody ListReq req) {
        // 判断是否有子类别
        Long count = sysNftTypeService.countKidsByCodes(sysNftTypeService.queryCodesByIds(req.getIds()));
        if (count > 0) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_40003_SUB_NFT_TYPE_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_40003_SUB_NFT_TYPE_EXIST.getCode(), null);
        }
        sysNftTypeService.batchDelete(req);
        return GenericDto.success(null);
    }

    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:nftType:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req) {
        // 停用类别判断是否有子类别
        Set<Long> ids = req.stream().filter(p -> SysStatusEnum.DISABLE.value() == p.getStatus()).map(SwitchReq::getId).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(ids)) {
            Long count = sysNftTypeService.countKidsByCodes(sysNftTypeService.queryCodesByIds(ids));
            if (count > 0) {
                return GenericDto.failure(AdminErrorCodeEnum.ERR_40003_SUB_NFT_TYPE_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_40003_SUB_NFT_TYPE_EXIST.getCode(), null);
            }
        }
        sysNftTypeService.enableOrDisable(req);
        return GenericDto.success(null);
    }

    @GetMapping("select")
    @ApiOperation("选取")
    @RequiredPermission("sys:nftType:select")
    public GenericDto<List<SysNftTypeResp>> select(@RequestParam(required = false) String parentCode) {
        return GenericDto.success(sysNftTypeService.queryRespByParentCode(parentCode));
    }

    @GetMapping("uc-dropdown")
    @ApiOperation("uc下拉列表")
    @Inner
    public GenericDto<List<SysNftTypeResp>> ucTypeDropdown() {
        return GenericDto.success(sysNftTypeService.queryRespList(null));
    }

}
