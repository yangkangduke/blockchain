package com.seeds.admin.controller;

import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.admin.dto.response.GameSrcResp;
import com.seeds.admin.dto.response.PreUploadResp;
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


    @PostMapping("list")
    @ApiOperation("游戏资源列表")
    public GenericDto<List<GameSrcResp>> queryPage(@RequestBody SysGameSrcPageReq req) {
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
    public GenericDto<Boolean> delete(@RequestParam(value = "fileName") String fileName) throws Exception {
        return GenericDto.success(gameFileService.delete(fileName));
    }

    @PostMapping("/pre-upload")
    @ApiOperation("使用预签名URL上传，不分段")
    public GenericDto<PreUploadResp> preUpload(@RequestBody UploadFileInfo req) {
        return GenericDto.success(gameFileService.preUpload(req));
    }


    @ApiOperation("创建分段上传")
    @PostMapping("/create-upload")
    public GenericDto<PreUploadResp> createUpload(@RequestBody UploadFileInfo req) {
        return GenericDto.success(gameFileService.createUpload(req));
    }

    @ApiOperation("为某个分段生成预签名的URL")
    @PostMapping("/get-part-url")
    public GenericDto<String> getPartUrl(@RequestBody FilePartReq req) {
        return GenericDto.success(gameFileService.getPartUrl(req));
    }

    @ApiOperation("完成分段上传")
    @PostMapping("/complete-upload")
    public GenericDto<String> completeMultipartUpload(@RequestBody CompleteUploadReq req) {
        return GenericDto.success(gameFileService.completeMultipartUpload(req));
    }

    @ApiOperation("中断上传")
    @PostMapping("/abort-upload")
    public GenericDto<Boolean> abortUpload(@RequestBody CompleteUploadReq req) {
        return GenericDto.success(gameFileService.abortUpload(req));
    }

    @PostMapping("add-new")
    @ApiOperation(value = "新增", notes = "前端直接上传，不再走后台上传逻辑")
    public GenericDto<Boolean> add(List<SysGameSrcAddReq> reqs) {
        gameFileService.add(reqs);
        return GenericDto.success(null);
    }
}
