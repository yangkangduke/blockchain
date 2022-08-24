package com.seeds.admin.controller;


import com.seeds.admin.service.SysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件 前端控制器
 * </p>
 *
 * @author hang.yu
 * @date 2022-08-24
 */
@RestController
@RequestMapping("/public/file")
@Api(tags = "公共文件")
public class PublicFileController {

    @Autowired
    private SysFileService sysFileService;

    @GetMapping("download/{bucketName}")
    @ApiOperation("下载")
    public void download(HttpServletResponse response, @PathVariable String bucketName, @RequestParam String objectName) {
        sysFileService.download(response, bucketName, objectName);
    }

}
