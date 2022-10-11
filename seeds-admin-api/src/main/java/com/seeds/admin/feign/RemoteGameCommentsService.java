package com.seeds.admin.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGameCommentsAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameCommentsLikeOrBuryReq;
import com.seeds.admin.dto.request.SysGameCommentsPageReq;
import com.seeds.admin.dto.request.SysGameCommentsRepliesReq;
import com.seeds.admin.dto.response.SysGameCommentsLikeOrBuryResp;
import com.seeds.admin.dto.response.SysGameCommentsRepliesResp;
import com.seeds.admin.dto.response.SysGameCommentsResp;
import com.seeds.admin.feign.interceptor.AdminFeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author hewei
 * @date 2022/8/26
 */
@FeignClient(name = "remoteGamCommentsService", url = "${Seeds-admin}", path = "/internal-game-comments", configuration = {AdminFeignInnerRequestInterceptor.class})
public interface RemoteGameCommentsService {

	@PostMapping("page")
	@ApiOperation("分页")
	GenericDto<Page<SysGameCommentsResp>> queryPage(@RequestBody SysGameCommentsPageReq req);

	@PostMapping("add")
	@ApiOperation("添加")
	GenericDto<Object> add(@Validated @RequestBody SysGameCommentsAddOrModifyReq req);


	@PostMapping("like")
	@ApiOperation("点赞")
	GenericDto<SysGameCommentsLikeOrBuryResp> like(@Validated @RequestBody SysGameCommentsLikeOrBuryReq req);

	@PostMapping("bury")
	@ApiOperation("踩")
	GenericDto<SysGameCommentsLikeOrBuryResp> bury(@Validated @RequestBody SysGameCommentsLikeOrBuryReq req);


	@PostMapping("reply")
	@ApiOperation("回复评论")
	GenericDto<Integer> reply(@Validated @RequestBody SysGameCommentsRepliesReq req);


	@GetMapping("get-replies")
	@ApiOperation("获取评论下的回复")
	GenericDto<List<SysGameCommentsRepliesResp>> getReplies(@RequestParam Long commentsId);

}
