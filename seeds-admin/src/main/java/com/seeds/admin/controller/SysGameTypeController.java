package com.seeds.admin.controller;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysGameTypeResp;
import com.seeds.admin.entity.SysGameTypeEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.service.SysGameTypeService;
import com.seeds.common.dto.GenericDto;
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
 * 游戏类别管理
 * @author hang.yu
 * @date 2022/8/25
 */
@Slf4j
@Api(tags = "游戏类别管理")
@RestController
@RequestMapping("/game-type")
public class SysGameTypeController {

    @Autowired
    private SysGameTypeService sysGameTypeService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:gameType:list")
    public GenericDto<List<SysGameTypeResp>> list(@RequestParam(required = false) String name) {
        return GenericDto.success(sysGameTypeService.queryRespList(name));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:gameType:add")
    public GenericDto<Object> add(@Valid @RequestBody SysGameTypeAddReq req) {
        // 查重
        SysGameTypeEntity gameType = sysGameTypeService.queryByTypeCode(req.getCode());
        if (gameType != null) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_80003_GAME_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_80003_GAME_TYPE_ALREADY_EXIST.getCode(), null);
        }
        sysGameTypeService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:gameType:detail")
    public GenericDto<SysGameTypeResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysGameTypeService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:gameType:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysGameTypeModifyReq req) {
        // 查重
        SysGameTypeEntity gameType = sysGameTypeService.queryByTypeCode(req.getCode());
        if (gameType != null && !Objects.equals(gameType.getId(), req.getId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_80003_GAME_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_80003_GAME_TYPE_ALREADY_EXIST.getCode(), null);
        }
        // 上级类别不能为自身
        if (req.getCode().equals(req.getParentCode())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_80004_GAME_TYPE_PARENT_ITSELF.getDescEn(), AdminErrorCodeEnum.ERR_80004_GAME_TYPE_PARENT_ITSELF.getCode(), null);
        }
        sysGameTypeService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:gameType:delete")
    public GenericDto<Object> delete(@Valid @RequestBody ListReq req) {
        // 判断是否有子类别
        Long count = sysGameTypeService.countKidsByCodes(sysGameTypeService.queryCodesByIds(req.getIds()));
        if (count > 0) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_80005_SUB_GAME_TYPE_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_80005_SUB_GAME_TYPE_EXIST.getCode(), null);
        }
        sysGameTypeService.batchDelete(req);
        return GenericDto.success(null);
    }

    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:gameType:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req) {
        // 停用类别判断是否有子类别
        Set<Long> ids = req.stream().filter(p -> SysStatusEnum.DISABLE.value() == p.getStatus()).map(SwitchReq::getId).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(ids)) {
            Long count = sysGameTypeService.countKidsByCodes(sysGameTypeService.queryCodesByIds(ids));
            if (count > 0) {
                return GenericDto.failure(AdminErrorCodeEnum.ERR_80005_SUB_GAME_TYPE_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_80005_SUB_GAME_TYPE_EXIST.getCode(), null);
            }
        }
        sysGameTypeService.enableOrDisable(req);
        return GenericDto.success(null);
    }

    @GetMapping("select")
    @ApiOperation("选取")
    @RequiredPermission("sys:gameType:select")
    public GenericDto<List<SysGameTypeResp>> select(@RequestParam(required = false) String parentCode) {
        return GenericDto.success(sysGameTypeService.queryRespByParentCode(parentCode));
    }

}
