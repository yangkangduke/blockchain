package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.internal.DeleteReq;
import com.seeds.game.dto.request.internal.ServerRoleCreateUpdateReq;
import com.seeds.game.dto.request.internal.ServerRolePageReq;
import com.seeds.game.dto.response.ServerRoleResp;
import com.seeds.game.service.IServerRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 游戏服角色 web端调用
 *
 * @author hewei
 * @since 2023-01-31
 */
@Api(tags = "游戏服角色相关接口，web调用")
@RestController
@RequestMapping("/web/server-role")
public class ServerRoleController {

    @Autowired
    private IServerRoleService serverRoleService;


    @PostMapping("page")
    @ApiOperation("获取角色分页信息")
    public GenericDto<IPage<ServerRoleResp>> create(@Valid @RequestBody ServerRolePageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(serverRoleService.queryPage(req));
    }


    @PostMapping("create")
    @ApiOperation("新增角色信息")
    public GenericDto<Object> createRole(@RequestBody @Valid ServerRoleCreateUpdateReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        serverRoleService.createRole(req);
        return GenericDto.success(null);
    }


    @PostMapping("update")
    @ApiOperation("修改角色信息")
    public GenericDto<Object> updateRole(@RequestBody @Valid ServerRoleCreateUpdateReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        serverRoleService.updateRole(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete/{id}")
    public GenericDto<Object> delete(@PathVariable Long id) {
        DeleteReq deleteReq = new DeleteReq();
        deleteReq.setUserId(UserContext.getCurrentUserId());
        deleteReq.setId(id);
        serverRoleService.delete(deleteReq);
        return GenericDto.success(null);
    }
}
