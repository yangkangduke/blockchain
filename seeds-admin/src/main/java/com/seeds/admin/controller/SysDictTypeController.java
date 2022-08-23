package com.seeds.admin.controller;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysDictTypeAddReq;
import com.seeds.admin.dto.request.SysDictTypeModifyReq;
import com.seeds.admin.dto.response.SysDictTypeResp;
import com.seeds.admin.entity.SysDictTypeEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.service.SysDictTypeService;
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
 * 字典类型
 * @author hang.yu
 * @date 2022/7/13
 */
@Slf4j
@Api(tags = "字典类型")
@RestController
@RequestMapping("/dict-type")
public class SysDictTypeController {

    @Autowired
    private SysDictTypeService sysDictTypeService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:dictType:list")
    public GenericDto<List<SysDictTypeResp>> list(@RequestParam(required = false) String name) {
        return GenericDto.success(sysDictTypeService.queryRespList(name));
    }

    //增加用户
    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:dictType:add")
    public GenericDto<Object> add(@Valid @RequestBody SysDictTypeAddReq req) {
        //查重
        SysDictTypeEntity dictType = sysDictTypeService.queryByTypeCode(req.getDictCode());
        if (dictType != null) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_60001_DICT_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_60001_DICT_TYPE_ALREADY_EXIST.getCode(), null);
        }
        sysDictTypeService.add(req);
        return GenericDto.success(null);
    }

    //查询
    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:dictType:detail")
    public GenericDto<SysDictTypeResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysDictTypeService.detail(id));
    }

    //修改
    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:dictType:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysDictTypeModifyReq req) {
        //查重
        SysDictTypeEntity dictType = sysDictTypeService.queryByTypeCode(req.getDictCode());
        if (dictType != null && !Objects.equals(dictType.getId(), req.getId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_60001_DICT_TYPE_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_60001_DICT_TYPE_ALREADY_EXIST.getCode(), null);
        }
        // 上级类别不能为自身
        if (req.getDictCode().equals(req.getParentCode())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_60002_DICT_TYPE_PARENT_ITSELF.getDescEn(), AdminErrorCodeEnum.ERR_60002_DICT_TYPE_PARENT_ITSELF.getCode(), null);
        }
       sysDictTypeService.modify(req);
        return GenericDto.success(null);
    }

    //删除用户
    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:dictType:delete")
    public GenericDto<Object> delete(@Valid @RequestBody ListReq req) {
        // 判断是否有子类别
        Long count = sysDictTypeService.countKidsByCodes(sysDictTypeService.queryCodesByIds(req.getIds()));
        if (count > 0) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_60003_SUB_DICT_TYPE_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_60003_SUB_DICT_TYPE_EXIST.getCode(), null);
        }
        sysDictTypeService.delete(req);
        return GenericDto.success(null);
    }
}
