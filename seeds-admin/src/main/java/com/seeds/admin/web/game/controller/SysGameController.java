package com.seeds.admin.web.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.common.SwitchReq;
import com.seeds.admin.dto.game.request.SysGameAddReq;
import com.seeds.admin.dto.game.request.SysGamePageReq;
import com.seeds.admin.dto.game.response.SysGameResp;
import com.seeds.admin.dto.merchant.request.SysMerchantModifyReq;
import com.seeds.admin.web.common.controller.AdminBaseController;
import com.seeds.admin.web.game.service.SysGameService;
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
 * 游戏管理
 * @author hang.yu
 * @date 2022/7/21
 */
@Slf4j
@Api("游戏管理")
@RestController
@RequestMapping("/game")
public class SysGameController extends AdminBaseController {

    @Autowired
    private SysGameService sysGameService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:game:page")
    public GenericDto<IPage<SysGameResp>> queryPage(@Valid @RequestBody SysGamePageReq query){
        return GenericDto.success(sysGameService.queryPage(query));
    }

    @GetMapping("list/{merchantId}")
    @ApiOperation("列表")
    @RequiredPermission("sys:game:list")
    public GenericDto<List<SysGameResp>> list(@PathVariable("merchantId") Long merchantId){
        return GenericDto.success(sysGameService.queryList(merchantId));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:game:add")
    public GenericDto<Object> add(@Valid @RequestBody SysGameAddReq req){
        sysGameService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:game:detail")
    public GenericDto<SysGameResp> detail(@PathVariable("id") Long id){
        return GenericDto.success(sysGameService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:game:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysMerchantModifyReq req){
        sysGameService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete/{id}")
    @ApiOperation("删除")
    @RequiredPermission("sys:game:delete")
    public GenericDto<Object> delete(@PathVariable("id") Long id){
        sysGameService.delete(id);
        return GenericDto.success(null);
    }

    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:game:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req){
        sysGameService.enableOrDisable(req);
        return GenericDto.success(null);
    }

}
