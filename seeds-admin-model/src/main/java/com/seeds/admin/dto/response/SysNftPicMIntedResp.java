package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yang.deng
 * @date 2023/2/20
 */
@Data
@ApiModel(value = "SysNftPicMIntedResp")
public class SysNftPicMIntedResp {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("文件名")
    private String picName;

    @ApiModelProperty("预览")
    private String url;

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

    @ApiModelProperty("NFT的类型：1:skin , 2:equip")
    private Integer nftType;


    @ApiModelProperty("confId")
    private Long confId;

    @ApiModelProperty("autoId")
    private Long autoId;

    @ApiModelProperty("未来NFT 发行的平台， 1,Magic Eden  2,Seeds")
    private Integer platform;

    @ApiModelProperty("链上地址")
    private String tokenAddress;

    @ApiModelProperty("mint时间")
    private Long mintTime;

    @ApiModelProperty("owner")
    private String owner;

    @ApiModelProperty("上架状态 0 未上架 1 已上架 2英式拍卖 3 取消拍卖")
    private Integer listState;
}
