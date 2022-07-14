package com.seeds.admin.web.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.sys.request.SysRoleAddReq;
import com.seeds.admin.dto.sys.request.SysRoleModifyReq;
import com.seeds.admin.dto.sys.request.SysRolePageReq;
import com.seeds.admin.dto.sys.response.SysRoleResp;
import com.seeds.admin.entity.sys.SysRoleEntity;
import com.seeds.admin.enums.AdminErrorCode;
import com.seeds.admin.web.sys.service.SysRoleService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author hang.yu
 * @date 2022/7/14
 */
@Slf4j
@RestController
@RequestMapping("/sys/role")
public class SyRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("page")
    @ApiOperation("分页")
    public GenericDto<IPage<SysRoleResp>> queryPage(@RequestBody SysRolePageReq query){
        return GenericDto.success(sysRoleService.queryPage(query));
    }

    @GetMapping("list")
    @ApiOperation("列表")
    public GenericDto<List<SysRoleResp>> list(){
        return GenericDto.success(sysRoleService.queryList());
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    public GenericDto<SysRoleResp> detail(@PathVariable("id") Long id){
        return GenericDto.success(sysRoleService.detail(id));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    public GenericDto<Object> add(@RequestBody SysRoleAddReq req){
        // 查重
        SysRoleEntity role = sysRoleService.queryByRoleCode(req.getRoleCode());
        if (role != null) {
            return GenericDto.failure(AdminErrorCode.ERR_20001_ROLE_ALREADY_EXIST.getDescEn(), AdminErrorCode.ERR_20001_ROLE_ALREADY_EXIST.getCode(), null);
        }
        sysRoleService.add(req);
        return GenericDto.success(null);
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    public GenericDto<Object> modify(@RequestBody SysRoleModifyReq req){
        // 查重
        SysRoleEntity role = sysRoleService.queryByRoleCode(req.getRoleCode());
        if (role != null) {
            return GenericDto.failure(AdminErrorCode.ERR_20001_ROLE_ALREADY_EXIST.getDescEn(), AdminErrorCode.ERR_20001_ROLE_ALREADY_EXIST.getCode(), null);
        }
        sysRoleService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    public GenericDto<Object> delete(@RequestBody Set<Long> ids){
        sysRoleService.batchDelete(ids);
        return GenericDto.success(null);
    }

}
