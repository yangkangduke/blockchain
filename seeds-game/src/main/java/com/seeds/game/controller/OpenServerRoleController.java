package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.OpenServerRoleCreateUpdateReq;
import com.seeds.game.dto.request.OpenServerRolePageReq;
import com.seeds.game.dto.response.ServerRoleResp;
import com.seeds.game.service.IServerRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 游戏服角色
 *
 * @author hewei
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "游戏服角色相关接口")
@RestController
@RequestMapping("/server-role")
public class OpenServerRoleController {

    @Autowired
    private IServerRoleService serverRoleService;

    @PostMapping("page")
    @ApiOperation("获取角色分页信息")
    public GenericDto<IPage<ServerRoleResp>> create(@Valid @RequestBody OpenServerRolePageReq req) {
        return GenericDto.success(serverRoleService.queryPage(req));
    }

    @PostMapping("create")
    @ApiOperation("新增角色信息")
    public GenericDto<Object> createRole(@RequestBody @Valid OpenServerRoleCreateUpdateReq req) {
        serverRoleService.createRole(req);
        return GenericDto.success(null);
    }

    @PostMapping("update")
    @ApiOperation("修改角色信息")
    public GenericDto<Object> updateRole(@RequestBody @Valid OpenServerRoleCreateUpdateReq req) {
        serverRoleService.updateRole(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete/{id}")
    public GenericDto<Object> delete(@PathVariable Long id) {
        serverRoleService.delete(id);
        return GenericDto.success(null);
    }
}
