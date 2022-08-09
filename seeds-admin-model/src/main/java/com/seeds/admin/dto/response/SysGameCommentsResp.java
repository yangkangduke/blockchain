package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel(value = "SysGameCommentsResp", description = "系统游戏评论信息")
public class SysGameCommentsResp {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏id")
    private Long gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * uc端用户id
     */
    @ApiModelProperty("uc端用户id")
    private String ucUserId;

    /**
     * 评价时间
     */
    @ApiModelProperty("评价时间")
    private Long commentsTime;

    /**
     * 用户评价
     */
    @ApiModelProperty("用户评价")
    private String comments;

    /**
     * 评分
     */
    @ApiModelProperty("评分")
    private BigDecimal star;

    /**
     * 评价状态  0：无效   1：有效
     */
    @ApiModelProperty("评价状态  0：无效   1：有效")
    private Integer status;
}