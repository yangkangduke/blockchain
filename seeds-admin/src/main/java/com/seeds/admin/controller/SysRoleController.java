package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysRoleAddReq;
import com.seeds.admin.dto.request.SysRoleModifyReq;
import com.seeds.admin.dto.request.SysRolePageReq;
import com.seeds.admin.dto.response.SysRoleResp;
import com.seeds.admin.entity.SysRoleEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.service.SysRoleService;
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
 * 角色管理
 * @author hang.yu
 * @date 2022/7/14
 */
@Slf4j
@Api("角色管理")
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:role:page")
    public GenericDto<IPage<SysRoleResp>> queryPage(@Valid @RequestBody SysRolePageReq query) {
        return GenericDto.success(sysRoleService.queryPage(query));
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:role:list")
    public GenericDto<List<SysRoleResp>> list() {
        return GenericDto.success(sysRoleService.queryList());
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:role:detail")
    public GenericDto<SysRoleResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysRoleService.detail(id));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:role:add")
    public GenericDto<Object> add(@Valid @RequestBody SysRoleAddReq req) {
        // 查重
        SysRoleEntity role = sysRoleService.queryByRoleCode(req.getRoleCode());
        if (role != null) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_20001_ROLE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_20001_ROLE_ALREADY_EXIST.getCode(), null);
        }
        sysRoleService.add(req);
        return GenericDto.success(null);
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:role:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysRoleModifyReq req) {
        // 查重
        SysRoleEntity role = sysRoleService.queryByRoleCode(req.getRoleCode());
        if (role != null && !Objects.equals(req.getId(), role.getId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_20001_ROLE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_20001_ROLE_ALREADY_EXIST.getCode(), null);
        }
        sysRoleService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:role:delete")
    public GenericDto<Object> delete(@Valid @RequestBody ListReq req) {
        sysRoleService.batchDelete(req.getIds());
        return GenericDto.success(null);
    }

}
