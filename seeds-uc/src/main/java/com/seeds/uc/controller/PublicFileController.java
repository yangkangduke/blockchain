package com.seeds.uc.controller;


import com.seeds.uc.service.IUcFileService;
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
 * @author yk
 * @date 2022-12-29
 */
@RestController
@RequestMapping("/public/file")
@Api(tags = "公共文件")
public class PublicFileController {

    @Autowired
    private IUcFileService iUcFileService;

    @GetMapping("/{bucket}/{objectName}")
    @ApiOperation(value = "获取文件", notes = "获取文件")
    public void file(@PathVariable String bucket, @PathVariable String objectName, HttpServletResponse response) {
        iUcFileService.getFile(bucket, objectName, response);
    }

}
