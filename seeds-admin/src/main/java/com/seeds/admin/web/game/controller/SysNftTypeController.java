package com.seeds.admin.web.game.controller;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.common.ListReq;
import com.seeds.admin.dto.common.SwitchReq;
import com.seeds.admin.dto.game.request.SysNftTypeAddReq;
import com.seeds.admin.dto.game.request.SysNftTypeModifyReq;
import com.seeds.admin.dto.game.response.SysNftTypeResp;
import com.seeds.admin.entity.game.SysNftTypeEntity;
import com.seeds.admin.enums.AdminErrorCode;
import com.seeds.admin.web.common.controller.AdminBaseController;
import com.seeds.admin.web.game.service.SysNftTypeService;
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
 * NFT类别管理
 * @author hang.yu
 * @date 2022/7/21
 */
@Slf4j
@Api("NFT类别管理")
@RestController
@RequestMapping("/nftType")
public class SysNftTypeController extends AdminBaseController {

    @Autowired
    private SysNftTypeService sysNftTypeService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:nftType:list")
    public GenericDto<List<SysNftTypeResp>> list() {
        return GenericDto.success(sysNftTypeService.queryRespList());
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:nftType:add")
    public GenericDto<Object> add(@Valid @RequestBody SysNftTypeAddReq req) {
        // 查重
        SysNftTypeEntity nftType = sysNftTypeService.queryByTypeCode(req.getCode());
        if (nftType != null) {
            return GenericDto.failure(AdminErrorCode.ERR_40001_NFT_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCode.ERR_40001_NFT_TYPE_ALREADY_EXIST.getCode(), null);
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
    @RequiredPermission("sys:game:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysNftTypeModifyReq req) {
        // 查重
        SysNftTypeEntity nftType = sysNftTypeService.queryByTypeCode(req.getCode());
        if (nftType != null) {
            return GenericDto.failure(AdminErrorCode.ERR_40001_NFT_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCode.ERR_40001_NFT_TYPE_ALREADY_EXIST.getCode(), null);
        }
        // 上级类别不能为自身
        if (req.getCode().equals(req.getParentCode())) {
            return GenericDto.failure(AdminErrorCode.ERR_40002_NFT_TYPE_PARENT_ITSELF.getDescEn(), AdminErrorCode.ERR_40002_NFT_TYPE_PARENT_ITSELF.getCode(), null);
        }
        sysNftTypeService.modify(req);
        return GenericDto.success(null);
    }

    /*@PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:game:delete")
    public GenericDto<Object> delete(@RequestBody ListReq req) {
        sysGameService.batchDelete(req);
        return GenericDto.success(null);
    }

    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:game:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req) {
        sysGameService.enableOrDisable(req);
        return GenericDto.success(null);
    }*/

}
