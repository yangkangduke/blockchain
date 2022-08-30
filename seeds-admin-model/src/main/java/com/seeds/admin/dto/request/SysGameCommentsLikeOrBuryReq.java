package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hewei
 * @date 2022/8/26
 */
@Data
@ApiModel(value = "SysGameCommentsLikeOrBuryReq", description = "评论赞、踩请求入参")
public class SysGameCommentsLikeOrBuryReq {

    @NotNull
    @ApiModelProperty(value = "游戏ID")
    private Long commentsId;

    @NotNull
    @ApiModelProperty(value = "评论者id")
    private Long ucUserId;

}
