package com.seeds.uc.controller;


import com.seeds.admin.dto.response.GameSrcLinkResp;
import com.seeds.admin.feign.RemoteGameSrcService;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 游戏 前端控制器
 * </p>
 *
 * @author hang.yu
 * @date 2022-08-22
 */
@RestController
@Api(tags = "游戏资源下载链接")
public class PublicGameSrcController {

    @Autowired
    private RemoteGameSrcService remoteGameSrcService;

    @GetMapping("/public/game-resource/get-link")
    @ApiOperation(value = "获取cnd上资源链接", notes = "type 1 首页视频 2 游戏安装包 3 游戏补丁")
    GenericDto<List<GameSrcLinkResp>> getLinks(HttpServletRequest request,
                                               @RequestParam(value = "type") Integer type) {

        String realClientIp = WebUtil.getIpAddr(request);
        return remoteGameSrcService.getLinks(realClientIp, type);
    }


}
