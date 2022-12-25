package com.seeds.admin.controller;

import com.seeds.admin.dto.response.GameSrcLinkResp;
import com.seeds.admin.service.GameSourceService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
@Api(tags = "游戏相关资源内部调用")
@RequestMapping("/internal-game-resource")
public class InterGameSourceController {

    @Autowired
    private GameSourceService gameSourceService;


    @GetMapping("get-links")
    @ApiOperation("获取需要请求的链接地址")
    @Inner
    public GenericDto<List<GameSrcLinkResp>> getLinks(@RequestParam(value = "ip") String ip,
                                                      @RequestParam(value = "type") Integer type) {
        return GenericDto.success(gameSourceService.getLinks(ip, type));
    }
}
