package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.request.SysGameSrcAddReq;
import com.seeds.admin.dto.request.SysGameSrcPageReq;
import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.admin.dto.response.GameSrcResp;
import com.seeds.admin.service.GameSourceService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 游戏相关文件上传
 * 首页宣传视频、游戏安装包等
 *
 * @author: hewei
 * @date 2022/12/17
 */
@Slf4j
@RestController
@Api(tags = "游戏相关资源")
@RequestMapping("/game-resource")
public class GameSourceController {

    @Autowired
    private GameSourceService gameFileService;


    @PostMapping("page")
    @ApiOperation("游戏资源分页列表")
    public GenericDto<IPage<GameSrcResp>> queryPage(@RequestBody SysGameSrcPageReq req) {
        return GenericDto.success(gameFileService.queryPage(req));
    }


    @PostMapping("add")
    @ApiOperation(value = "新增", notes = "前端需要支持可以上传文件夹")
    public GenericDto<Boolean> upload(@RequestPart(value = "files") MultipartFile[] files, @Valid SysGameSrcAddReq req) {
        gameFileService.upload(files, req);
        return GenericDto.success(null);
    }


    @PostMapping("switch")
    @ApiOperation("启用、禁用")
    public GenericDto<Boolean> switchStatus(@Valid @RequestBody SwitchReq req) {
        return GenericDto.success(gameFileService.switchStatus(req));
    }

//    @GetMapping("get-links")
//    @ApiOperation("获取需要请求的链接地址")
//    public GenericDto<List<GameSrcLinkResp>> getLinks(HttpServletRequest request,
//                                                      @RequestParam(value = "type 1,首页视频地址 2 游戏安装包 3 游戏补丁") Integer type) {
//        return GenericDto.success(gameFileService.getLinks(request, type));
//    }


    @PostMapping("get-all-from-s3")
    @ApiOperation("获取各个源S3上所有的文件")
    public GenericDto<List<GameFileResp>> getAll() {
        return GenericDto.success(gameFileService.getAll());
    }

    @PostMapping("delete")
    @ApiOperation("删除S3上源文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "fileName", value = "参数值传列表中对应列的key的值", dataType = "String")
    })
    public GenericDto<Boolean> delete(@RequestParam(value = "fileName") String fileName) {
        gameFileService.delete(fileName);
        return GenericDto.success(null);
    }

//    @PostMapping("get-patch-from-s3")
//    @ApiOperation("获取各个源S3上所有的补丁文件，不对接")
//    public GenericDto<List<GameFileResp>> getAllPatch() {
//        return GenericDto.success(gameFileService.getAllPatch());
//    }
}
