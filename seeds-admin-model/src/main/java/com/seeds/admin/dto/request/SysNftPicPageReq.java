package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SysNftPicPageReq", description = "NFT上架分页请求入参")
public class SysNftPicPageReq {

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("属性")
    private String symbol;

    @ApiModelProperty("游戏内物品的唯一id")
    private Long autoId;

    @ApiModelProperty("游戏那边的conf_id")
    private Long confId;

    @ApiModelProperty("tokenAddress")
    private String tokenAddress;

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "数据条数")
    private Integer size = 50;
}
