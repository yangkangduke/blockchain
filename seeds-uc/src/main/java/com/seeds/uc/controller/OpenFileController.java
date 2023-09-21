package com.seeds.uc.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.UcFileQueryResp;
import com.seeds.uc.dto.request.UcFileResp;
import com.seeds.uc.model.UcFile;
import com.seeds.uc.service.IUcFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * file table 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-08-05
 */
@RestController
@Api(tags = "文件")
@RequestMapping("/file")
public class OpenFileController {

    @Autowired
    private IUcFileService ucFileService;


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public GenericDto<Page<UcFile>> getSysFilePage(UcFileQueryResp fileQueryResp) {
        Page<UcFile> page = new Page();
        page.setSize(fileQueryResp.getSize());
        page.setCurrent(fileQueryResp.getCurrent());
        UcFile ucFile = UcFile.builder().build();
        BeanUtil.copyProperties(fileQueryResp, ucFile);
        return GenericDto.success(ucFileService.page(page, Wrappers.query(ucFile)));
    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public GenericDto<UcFileResp> upload(@RequestPart("file") MultipartFile file) {
        return ucFileService.upload(file);
    }

    @GetMapping("/{bucket}/{objectName}")
    @ApiOperation(value = "获取文件", notes = "获取文件")
    public void file(@PathVariable String bucket, @PathVariable String objectName, HttpServletResponse response) {
        ucFileService.getFile(bucket, objectName, response);
    }

    @DeleteMapping("delete-by-id/{id}")
    @ApiOperation(value = "通过id删除文件", notes = "通过id删除文件")
    public GenericDto<Boolean> deleteFileById(@PathVariable Long id) throws Exception {
        return GenericDto.success(ucFileService.deleteFileById(id));
    }

    @DeleteMapping("delete-by-name/{objectName}")
    @ApiOperation(value = "通过名字删除文件", notes = "通过名字删除文件")
    public GenericDto<Boolean> deleteFileByName(@PathVariable String objectName) throws Exception {
        return GenericDto.success(ucFileService.deleteFileByName(objectName));
    }


}