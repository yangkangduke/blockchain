package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hewei
 * @date 2022/8/8
 */
@Data
@ApiModel(value = "SysGameCommentsPageReq", description = "游戏评论分页请求入参")
public class SysGameCommentsPageReq extends PageReq implements Serializable {

    private String keyWords;

    @ApiModelProperty(value = "游戏ID")
    private Integer gameId;

    @ApiModelProperty(value = "评价状态  0：无效   1：有效")
    private Integer status;

    @ApiModelProperty(value = "排序类型 1.Newest 2.Top Rank，uc端查询时需要")
    private Integer sortType;

    @ApiModelProperty(value = "如果用户已经登录，前端需要传")
    private Long ucUserId;

}
