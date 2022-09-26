package com.seeds.uc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGameCommentsAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameCommentsLikeOrBuryReq;
import com.seeds.admin.dto.request.SysGameCommentsPageReq;
import com.seeds.admin.dto.request.SysGameCommentsRepliesReq;
import com.seeds.admin.dto.response.SysGameCommentsLikeOrBuryResp;
import com.seeds.admin.dto.response.SysGameCommentsRepliesResp;
import com.seeds.admin.dto.response.SysGameCommentsResp;
import com.seeds.admin.feign.RemoteGameCommentsService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.GameCommentsReq;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IUcUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: hewei
 * @date 2022/8/26
 */
@RestController
@Slf4j
@Api(tags = "游戏评论管理-uc")
public class PublicGameCommentsController {
    @Resource
    private RemoteGameCommentsService remoteGameCommentsService;
    @Resource
    private IUcUserService ucUserService;

    @PostMapping("/public/game-comments/page")
    @ApiOperation("分页列表")
    public GenericDto<Page<SysGameCommentsResp>> queryPage(@RequestBody SysGameCommentsPageReq req) {
        return remoteGameCommentsService.queryPage(req);
    }

    @PostMapping("game-comments/add")
    @ApiOperation("添加")
    public GenericDto<Object> add(@Validated @RequestBody GameCommentsReq req) {
        Long userId = UserContext.getCurrentUserId();
        SysGameCommentsAddOrModifyReq addReq = new SysGameCommentsAddOrModifyReq();
        if (req != null) {
            BeanUtils.copyProperties(req, addReq);
            addReq.setUcUserId(userId);
            UcUser user = ucUserService.getById(userId);
            if (user != null) {
                addReq.setUcUserName(user.getNickname());
            }
        }
        return remoteGameCommentsService.add(addReq);
    }


    @PostMapping("game-comments/like/{id}")
    @ApiOperation("点赞/有用")
    public GenericDto<SysGameCommentsLikeOrBuryResp> like(@ApiParam(value = "评论id") @PathVariable("id") Long id) {
        Long userId = UserContext.getCurrentUserId();
        SysGameCommentsLikeOrBuryReq req = new SysGameCommentsLikeOrBuryReq();
        req.setCommentsId(id);
        req.setUcUserId(userId);
        return remoteGameCommentsService.like(req);
    }

    @PostMapping("game-comments/bury/{id}")
    @ApiOperation("无用")
    public GenericDto<SysGameCommentsLikeOrBuryResp> bury(@ApiParam(value = "评论id") @PathVariable("id") Long id) {
        Long userId = UserContext.getCurrentUserId();
        SysGameCommentsLikeOrBuryReq req = new SysGameCommentsLikeOrBuryReq();
        req.setCommentsId(id);
        req.setUcUserId(userId);
        return remoteGameCommentsService.bury(req);
    }

    @PostMapping("game-comments/reply")
    @ApiOperation("回复评论")
    public GenericDto<Integer> reply(@Validated @RequestBody SysGameCommentsRepliesReq req) {
        Long userId = UserContext.getCurrentUserId();
        req.setUcUserId(userId);
        UcUser user = ucUserService.getById(userId);
        if (user != null) {
            req.setUcUserName(user.getNickname());
        }
        return remoteGameCommentsService.reply(req);
    }

    @GetMapping("/public/game-comments/get-replies/{id}")
    @ApiOperation("获取评论下的回复")
    public GenericDto<List<SysGameCommentsRepliesResp>> getReplies(@ApiParam(value = "评论id") @PathVariable("id") Long id) {
        return remoteGameCommentsService.getReplies(id);
    }
}
