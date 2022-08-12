package com.seeds.admin.controller;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.response.SysFileResp;
import com.seeds.admin.service.SysFileService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 系统文件
 * @author hang.yu
 * @date 2022/8/02
 */
@Slf4j
@Api(tags = "系统文件")
@RestController
@RequestMapping("/file")
public class SysFileController {

    @Autowired
    private SysFileService sysFileService;

    @PostMapping("upload/{type}")
    @ApiOperation("上传")
    @RequiredPermission("sys:file:upload")
    public GenericDto<SysFileResp> upload(@RequestBody MultipartFile file, @PathVariable String type) {
        return GenericDto.success(sysFileService.upload(file, type));
    }

    @GetMapping("download/{bucketName}/{objectName}")
    @ApiOperation("下载")
    @RequiredPermission("sys:file:download")
    public void download(HttpServletResponse response, @PathVariable String bucketName, @PathVariable String objectName) {
        sysFileService.download(response, bucketName, objectName);
    }

    @PostMapping("delete/{fileId}")
    @ApiOperation("删除")
    @RequiredPermission("sys:file:delete")
    public GenericDto<String> delete(@PathVariable Long fileId) {
        sysFileService.delete(fileId);
        return GenericDto.success(null);
    }

}
