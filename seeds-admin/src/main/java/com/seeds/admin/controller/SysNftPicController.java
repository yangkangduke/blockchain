package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.SysNftPicAttributeModifyReq;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.SysNftPicPageReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Slf4j
@Api(tags = "NFT上架的管理")
@RestController
@RequestMapping("/nft-pic")
public class SysNftPicController {

    @Autowired
    private SysNftPicService sysNftPicService;

    @PostMapping("page")
    @ApiOperation("获取分页信息")
    public GenericDto<IPage<SysNftPicResp>> queryPage(@RequestBody SysNftPicPageReq req) {
        return GenericDto.success(sysNftPicService.queryPage(req));
    }

    @PostMapping("uploadCSV")
    @ApiOperation(value = "上传属性文件", notes = "type: 1 属性csv; 2 autoId")
    public GenericDto<Boolean> upload(@RequestPart(value = "file") MultipartFile file, @RequestParam(value = "type  1 属性csv; 2 autoId") @NotNull Integer type) {
        sysNftPicService.upload(file, type);
        return GenericDto.success(null);
    }


    @GetMapping("get-json/{id}")
    @ApiOperation("获取属性JSON文件")
    public GenericDto<String> getAttr(@PathVariable("id") Long id) {
        return GenericDto.success(sysNftPicService.getAttr(id));
    }

    @PostMapping("update")
    @ApiOperation("修改内置属性")
    public GenericDto<Object> updateAttribute(@RequestBody @Valid SysNftPicAttributeModifyReq req){
        sysNftPicService.updateAttribute(req);
        return GenericDto.success(null);
    }
}
