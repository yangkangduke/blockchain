package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物品分配
 *
 * @author: hewei
 * @date 2023/7/6
 */
@Data
public class OpenNftBuySuccessResp {

    @ApiModelProperty("autoId")
    private Long autoId;

    @ApiModelProperty("图片地址")
    private String imgUrl;

    @ApiModelProperty("属性：稀有度：normal,rare,epic")
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
}
