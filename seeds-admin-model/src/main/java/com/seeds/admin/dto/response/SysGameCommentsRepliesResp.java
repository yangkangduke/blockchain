package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(value = "SysGameCommentsRepliesResp", description = "系统游戏评论回复信息")
public class SysGameCommentsRepliesResp implements Serializable {

    private Long id;

    @ApiModelProperty("评论id")
    private Long commentsId;

    @ApiModelProperty("uc端用户ID")
    private Long ucUserId;

    @ApiModelProperty("uc端用户名字")
    private String ucUserName;

    @ApiModelProperty("回复时间")
    private Long replyTime;

    @ApiModelProperty("回复内容")
    private String replies;
}