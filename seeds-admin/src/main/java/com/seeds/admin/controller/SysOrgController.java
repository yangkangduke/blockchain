package com.seeds.admin.controller;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.annotation.SeedsOperationLog;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysOrgAddOrModifyReq;
import com.seeds.admin.dto.response.SysOrgResp;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.service.SysOrgService;
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
 * 组织管理
 *
 * @author: hewei
 * @date: 2022/7/22
 */
@Slf4j
@Api(tags = "组织管理")
@RestController
@RequestMapping("/org")
public class SysOrgController {

    @Autowired
    private SysOrgService orgService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:org:list")
    public GenericDto<List<SysOrgResp>> list(String orgName) {
        return GenericDto.success(orgService.queryRespList(orgName));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @SeedsOperationLog("添加组织")
    @RequiredPermission("sys:org:add")
    public GenericDto<Object> add(@Valid @RequestBody SysOrgAddOrModifyReq req) {
        orgService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:org:detail")
    public GenericDto<SysOrgResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(orgService.detail(id));
    }

    @PutMapping("modify")
    @ApiOperation("编辑")
    @SeedsOperationLog("编辑组织信息")
    @RequiredPermission("sys:org:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysOrgAddOrModifyReq req) {
        // 上级类别不能为自身
        if (Objects.equals(req.getOrgId(), req.getParentOrgId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_110001_ORG_PARENT_ITSELF.getDescEn(), AdminErrorCodeEnum.ERR_110001_ORG_PARENT_ITSELF.getCode(), null);
        }
        orgService.modify(req);
        return GenericDto.success(null);
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    @SeedsOperationLog("删除组织")
    @RequiredPermission("sys:org:delete")
    public GenericDto<Object> delete(@Valid @RequestBody ListReq req) {
        orgService.delete(req);
        return GenericDto.success(null);
    }
}
