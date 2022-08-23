package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.DataFilter;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysDictDataAddReq;
import com.seeds.admin.dto.request.SysDictDataModifyReq;
import com.seeds.admin.dto.request.SysDictDataPageReq;
import com.seeds.admin.dto.response.SysDictDataResp;
import com.seeds.admin.entity.SysDictDataEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.service.SysDictDataService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


/**
 * 字典数据
 * @author yang.deng
 * @date 2022/8/15
 */

@Slf4j
@Api(tags = "字典数据")
@RestController
@RequestMapping("/dict-data")
public class SysDictDataController {

    @Autowired
    private SysDictDataService  sysDictDataService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:dictData:list")
    public GenericDto<List<SysDictDataResp>> list(String name) {
        return GenericDto.success(sysDictDataService.queryRespList(name));
    }

    //分页
    @PostMapping("page")
    @ApiOperation("分页")
    @DataFilter
    @RequiredPermission("sys:dictData:page")
    public GenericDto<IPage<SysDictDataResp>> queryPage(@Valid @RequestBody SysDictDataPageReq query) {
        return GenericDto.success(sysDictDataService.queryPage(query));
    }

    //新增
    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:dictData:add")
    public GenericDto<Object> add(@Valid @RequestBody SysDictDataAddReq req) {
        //查重
        SysDictDataEntity dictData = sysDictDataService.queryByDictTypeIdAndLabel(req.getDictTypeId(), req.getDictLabel());
        if (dictData != null) {
            //已经存在，则无法添加
            return GenericDto.failure(AdminErrorCodeEnum.ERR_100001_DICT_DATA_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_100001_DICT_DATA_ALREADY_EXIST.getCode(), null);
        }
        sysDictDataService.add(req);
        return GenericDto.success(null);
    }

    //查询
    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:dictData:detail")
    public GenericDto<SysDictDataResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysDictDataService.detail(id));
    }

    //编辑
    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:dictData:modify")
    public GenericDto<Object> modify(@Validated @RequestBody SysDictDataModifyReq req) {
        //查重
        SysDictDataEntity dictData = sysDictDataService.queryByDictTypeIdAndLabel(req.getDictTypeId(), req.getDictLabel());
        if (dictData != null && !Objects.equals(req.getId(), dictData.getId())) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_100001_DICT_DATA_ALREADY_EXIST.getDescEn(), AdminErrorCodeEnum.ERR_100001_DICT_DATA_ALREADY_EXIST.getCode(), null);
        }
        sysDictDataService.modify(req);
        return GenericDto.success(null);
    }

    //删除
    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:dictData:delete")
    public GenericDto<Object> delete(@RequestBody ListReq req) {
        sysDictDataService.delete(req);
        return GenericDto.success(null);
    }
}
