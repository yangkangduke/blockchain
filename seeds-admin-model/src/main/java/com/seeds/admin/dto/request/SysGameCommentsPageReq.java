package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hewei
 * @date 2022/8/8
 */
@Data
@ApiModel(value = "SysGameCommentsPageReq", description = "游戏评论分页请求入参")
public class SysGameCommentsPageReq extends PageReq {

    private String keyWords;

    private Integer gameId;

    @ApiModelProperty(value = "评价状态  0：无效   1：有效")
    private Integer status;


}
