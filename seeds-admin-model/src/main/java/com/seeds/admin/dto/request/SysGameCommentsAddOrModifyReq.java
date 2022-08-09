package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;


/**
 * @author: hewei
 * @date: 2022/8/8
 */

@Data
@ApiModel(value = "SysGameCommentsAddOrModifyReq", description = "系统游戏评论信息")
public class SysGameCommentsAddOrModifyReq {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "游戏id", required = true)
    @NotNull(message = "gameId can not be empty")
    private Long gameId;

    @ApiModelProperty(value = "游戏名称", required = true)
    @NotBlank(message = "gameName can not be empty")
    private String gameName;

    @ApiModelProperty(value = "uc端用户id", required = true)
    @NotBlank(message = "ucUserId can not be empty")
    private String ucUserId;

    @ApiModelProperty(value = "用户评价，10-1024个字符", required = true)
    @NotBlank(message = " the number of words must be between 10 and 1024")
    @Size(min = 10, max = 1024)
    private String comments;

    @ApiModelProperty(value = "评分,0-5分 可以有一位小数如 4.5", required = true)
    @Max(value = 5, message = "score cannot be greater than 5 points")
    @Min(value = 0, message = "score cannot be less than 0 points")
    private BigDecimal star;

    @ApiModelProperty("评价状态  0：无效   1：有效")
    private Integer status;
}