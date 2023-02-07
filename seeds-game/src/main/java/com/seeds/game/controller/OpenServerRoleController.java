package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.OpenServerRoleCreateUpdateReq;
import com.seeds.game.dto.request.OpenServerRolePageReq;
import com.seeds.game.dto.request.internal.DeleteReq;
import com.seeds.game.dto.response.ServerRoleResp;
import com.seeds.game.service.IServerRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 游戏服角色 游戏方调用，需要带上 accessKey、signature、timestamp
 *
 * @author hewei
 * @since 2023-01-31
 */
@Slf4j
@Api(tags = "游戏服角色相关接口，游戏方调用")
@RestController
@RequestMapping("/server-role")
public class OpenServerRoleController {

    @Autowired
    private IServerRoleService serverRoleService;

    @PostMapping("page")
    @ApiOperation("获取角色分页信息")
    public GenericDto<IPage<ServerRoleResp>> queryPage(@Valid @RequestBody OpenServerRolePageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(serverRoleService.queryPage(req));
    }

    @PostMapping("list")
    @ApiOperation("获取角色列表，不分页")
    public GenericDto<List<ServerRoleResp>> queryList(@Valid @RequestBody OpenServerRolePageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(serverRoleService.queryList(req));
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

//    @PostMapping("delete")
//    @ApiOperation("删除角色信息")
//    public GenericDto<Object> delete(@RequestParam Long id,
//                                     @RequestParam String accessKey,
//                                     @RequestParam String signature,
//                                     @RequestParam Long timestamp) {
//        DeleteReq deleteReq = new DeleteReq();
//        deleteReq.setUserId(UserContext.getCurrentUserId());
//        deleteReq.setId(id);
//        serverRoleService.delete(deleteReq);
//        return GenericDto.success(null);
//    }
}
