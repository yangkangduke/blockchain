package com.seeds.admin.controller;

import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.admin.service.GameFileService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 游戏相关文件上传
 * 首页宣传视频、游戏安装包等
 *
 * @author: hewei
 * @date 2022/12/17
 */
@Slf4j
@RestController
@RequestMapping("/game-file")
public class GameFileController {

    @Autowired
    private GameFileService gameFileService;

    @PostMapping("upload")
    @ApiOperation("上传")
    public GenericDto<GameFileResp> upload(@RequestPart("file") MultipartFile file) {
        return GenericDto.success(gameFileService.upload(file));
    }

}
