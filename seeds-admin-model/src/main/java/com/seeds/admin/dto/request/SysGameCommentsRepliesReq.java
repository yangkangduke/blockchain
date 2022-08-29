package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hewei
 * @date 2022/8/26
 */
@Data
@ApiModel(value = "SysGameCommentsRepliesReq", description = "评论回复请求入参")
public class SysGameCommentsRepliesReq {

    @NotNull
    @ApiModelProperty(value = "评论ID")
    private Long commentsId;

    @ApiModelProperty(value = "回复者ID")
    private Long ucUserId;

    @ApiModelProperty(value = "回复者名字")
    private String ucUserName;

    @NotEmpty
    @ApiModelProperty(value = "回复内容")
    private String replies;
}
