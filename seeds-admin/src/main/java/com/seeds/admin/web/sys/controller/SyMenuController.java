package com.seeds.admin.web.sys.controller;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.sys.request.SysMenuAddReq;
import com.seeds.admin.dto.sys.request.SysMenuModifyReq;
import com.seeds.admin.dto.sys.response.SysMenuResp;
import com.seeds.admin.entity.sys.SysMenuEntity;
import com.seeds.admin.enums.AdminErrorCode;
import com.seeds.admin.web.common.controller.AdminBaseController;
import com.seeds.admin.web.sys.service.SysMenuService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author hang.yu
 * @date 2022/7/14
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class SyMenuController extends AdminBaseController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:menu:list")
    public GenericDto<List<SysMenuResp>> list(Integer type){
        return GenericDto.success(sysMenuService.queryList(type));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:menu:add")
    public GenericDto<Object> add(@Valid @RequestBody SysMenuAddReq req){
        // 查重
        SysMenuEntity menu = sysMenuService.queryByMenuCode(req.getCode());
        if (menu != null) {
            return GenericDto.failure(AdminErrorCode.ERR_30001_MENU_ALREADY_EXIST.getDescEn(), AdminErrorCode.ERR_30001_MENU_ALREADY_EXIST.getCode(), null);
        }
        sysMenuService.add(req);
        return GenericDto.success(null);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:menu:detail")
    public GenericDto<SysMenuResp> detail(@PathVariable("id") Long id){
        return GenericDto.success(sysMenuService.detail(id));
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:menu:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysMenuModifyReq req){
        // 查重
        SysMenuEntity menu = sysMenuService.queryByMenuCode(req.getCode());
        if (menu != null && !Objects.equals(req.getId(), menu.getId())) {
            return GenericDto.failure(AdminErrorCode.ERR_30001_MENU_ALREADY_EXIST.getDescEn(), AdminErrorCode.ERR_30001_MENU_ALREADY_EXIST.getCode(), null);
        }
        // 上级菜单不能为自身
        if (req.getId().equals(req.getPid())) {
            return GenericDto.failure(AdminErrorCode.ERR_30002_MENU_PARENT_ITSELF.getDescEn(), AdminErrorCode.ERR_30002_MENU_PARENT_ITSELF.getCode(), null);
        }
        sysMenuService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("delete/{id}")
    @ApiOperation("删除")
    @RequiredPermission("sys:menu:delete")
    public GenericDto<Object> delete(@PathVariable("id") Long id){
        // 判断是否有子菜单或按钮
        List<SysMenuEntity> list = sysMenuService.queryListByPid(id);
        if(!CollectionUtils.isEmpty(list)){
            return GenericDto.failure(AdminErrorCode.ERR_30003_SUB_MENU_EXIST.getDescEn(), AdminErrorCode.ERR_30003_SUB_MENU_EXIST.getCode(), null);
        }
        sysMenuService.delete(id);
        return GenericDto.success(null);
    }

}
