package com.seeds.admin.controller;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysMenuAddReq;
import com.seeds.admin.dto.request.SysMenuModifyReq;
import com.seeds.admin.dto.response.SysMenuBriefResp;
import com.seeds.admin.dto.response.SysMenuResp;
import com.seeds.admin.entity.SysMenuEntity;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.service.SysMenuService;
import com.seeds.admin.service.SysUserService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 菜单管理
 * @author hang.yu
 * @date 2022/7/14
 */
@Slf4j
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:menu:list")
    public GenericDto<List<SysMenuResp>> list(Integer type) {
        return GenericDto.success(sysMenuService.queryRespList(type));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:menu:add")
    public GenericDto<Object> add(@Valid @RequestBody SysMenuAddReq req) {
        // 查重
        SysMenuEntity menu = sysMenuService.queryByMenuCode(req.getCode());
        if (menu != null) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_30001_MENU_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_30001_MENU_ALREADY_EXIST.getCode(), null);
        }
        sysMenuService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:menu:detail")
    public GenericDto<SysMenuResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysMenuService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:menu:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysMenuModifyReq req) {
        // 查重
        SysMenuEntity menu = sysMenuService.queryByMenuCode(req.getCode());
        if (menu != null && !Objects.equals(req.getId(), menu.getId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_30001_MENU_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_30001_MENU_ALREADY_EXIST.getCode(), null);
        }
        // 上级菜单不能为自身
        if (req.getCode().equals(req.getParentCode())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_30002_MENU_PARENT_ITSELF.getDescEn(), AdminErrorCodeEnum.ERR_30002_MENU_PARENT_ITSELF.getCode(), null);
        }
        sysMenuService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:menu:delete")
    public GenericDto<Object> delete(@Valid @RequestBody ListReq req) {
        // 判断是否有子菜单或按钮
        Long count = sysMenuService.countKidsByCodes(sysMenuService.queryCodesByIds(req.getIds()));
        if (count > 0) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_30003_SUB_MENU_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_30003_SUB_MENU_EXIST.getCode(), null);
        }
        sysMenuService.batchDelete(req);
        return GenericDto.success(null);
    }

    @GetMapping("select")
    @ApiOperation("选取")
    @RequiredPermission("sys:menu:select")
    public GenericDto<List<SysMenuBriefResp>> select() {
        // 获取登录用户
        Long userId = UserContext.getCurrentAdminUserId();
        // 获取用户
        SysUserEntity user = sysUserService.queryById(userId);
        return GenericDto.success(sysMenuService.getUserMenuList(user));
    }

}
