package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SysNftPicPageReq", description = "NFT上架分页请求入参")
public class SysNftPicPageReq {

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("属性：稀有度分别为：normal,rare,epic")
    private String rarity;

    @ApiModelProperty("属性:图片特征-主题")
    private String feature;

    @ApiModelProperty("图片特征-主要配饰")
    private String accessories;

    @ApiModelProperty("图片特征-肤色")
    private String color;

    @ApiModelProperty("图片特征-装饰物")
    private String decorate;

    @ApiModelProperty("图片特征-其他")
    private String other;

    @ApiModelProperty("对应英雄的名字")
    private String hero;

    @ApiModelProperty("对应皮肤的名字")
    private String skin;

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
