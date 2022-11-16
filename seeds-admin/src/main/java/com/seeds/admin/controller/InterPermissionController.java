package com.seeds.admin.controller;

import com.seeds.admin.service.SysRoleService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 权限内部调用
 * @author hang.yu
 * @date 2022/11/10
 */
@Slf4j
@Api(tags = "权限内部调用")
@RestController
@RequestMapping("/internal-permission")
public class InterPermissionController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("entitled-users")
    @ApiOperation("获取拥有指定权限的用户")
    @Inner
    public GenericDto<Set<Long>> entitledUsers(@RequestParam String roleCode) {
        return GenericDto.success(sysRoleService.queryUsersByRole(roleCode));
    }

}
