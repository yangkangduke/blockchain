package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.SysGameCommentsAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameCommentsLikeOrBuryReq;
import com.seeds.admin.dto.request.SysGameCommentsPageReq;
import com.seeds.admin.dto.request.SysGameCommentsRepliesReq;
import com.seeds.admin.dto.response.SysGameCommentsLikeOrBuryResp;
import com.seeds.admin.dto.response.SysGameCommentsRepliesResp;
import com.seeds.admin.dto.response.SysGameCommentsResp;
import com.seeds.admin.service.SysGameCommentsLikeBuryService;
import com.seeds.admin.service.SysGameCommentsRepliesService;
import com.seeds.admin.service.SysGameCommentsService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 游戏评论 供uc端rpc调用
 *
 * @author: hewei
 * @date: 2022/8/25
 */
@Slf4j
@Api(tags = "游戏评论-rpc")
@RestController
@RequestMapping("/internal-game-comments")
public class InterGameCommentsController {

    @Resource
    private SysGameCommentsService gameCommentsService;

    @Resource
    private SysGameCommentsLikeBuryService likeBuryService;

    @Resource
    private SysGameCommentsRepliesService repliesService;

    @PostMapping("page")
    @ApiOperation("分页")
    @Inner
    public GenericDto<IPage<SysGameCommentsResp>> queryPage(@RequestBody SysGameCommentsPageReq req) {
        return GenericDto.success(gameCommentsService.queryPageForUc(req));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @Inner
    public GenericDto<Object> add(@Validated @RequestBody SysGameCommentsAddOrModifyReq req) {
        gameCommentsService.add(req);
        return GenericDto.success(null);
    }


    @PostMapping("like")
    @ApiOperation("点赞")
    @Inner
    public GenericDto<SysGameCommentsLikeOrBuryResp> like(@Validated @RequestBody SysGameCommentsLikeOrBuryReq req) {

        return GenericDto.success(likeBuryService.like(req));
    }

    @PostMapping("bury")
    @ApiOperation("点赞")
    @Inner
    public GenericDto<SysGameCommentsLikeOrBuryResp> bury(@Validated @RequestBody SysGameCommentsLikeOrBuryReq req) {

        return GenericDto.success(likeBuryService.bury(req));
    }

    @PostMapping("reply")
    @ApiOperation("回复评论")
    @Inner
    public GenericDto<Integer> reply(@Validated @RequestBody SysGameCommentsRepliesReq req) {

        return GenericDto.success(repliesService.reply(req));
    }

    @GetMapping("get-replies")
    @ApiOperation("获取评论下的回复")
    @Inner
    public GenericDto<List<SysGameCommentsRepliesResp>> getReplies(@RequestParam Long commentsId) {
        return GenericDto.success(repliesService.getReplies(commentsId));
    }

}
