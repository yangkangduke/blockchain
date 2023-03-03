package com.seeds.admin.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yang.deng
 * @date 2023/2/21
 */
@Data
@ApiModel(value = "SysNftPicAttributeModifyReq", description = "NFT内置属性修改请求入参")
public class SysNftPicAttributeModifyReq {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("属性：稀有度为下拉选框分别为：normal,rare,epic")
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

    @ApiModelProperty("mint 后的nft名字")
    private String name;

    @ApiModelProperty("symbol")
    private String symbol;

    @ApiModelProperty("description")
    private String description;

}
