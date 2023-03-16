package com.seeds.game.dto.request.internal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * NFT公共背包
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@Data
@ApiModel(value = "NftPublicBackpack对象", description = "NFT公共背包")
public class NftPublicBackpackReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称", example = "nftName")
    private String name;

    @ApiModelProperty(value = "描述", example = "测试NFT")
    private String desc;

    @ApiModelProperty(value = "icon", example = "XXXXX")
    private String icon;

    @ApiModelProperty(value = "图片", example = "测试图片")
    private String image;

    @ApiModelProperty(value = "类型：1装备 2道具 3英雄", example = "1")
    private Integer type;

    @ApiModelProperty(value = "对应游戏那边的conf_id，找静态表中的数据", example = "1002101")
    private Long itemId;

    @ApiModelProperty(value = "游戏那边传的。大区+game+流水", example = "10101201")
    private Long autoId;

    @ApiModelProperty(value = "用户id", example = "10")
    private Long userId;

    @ApiModelProperty(value = "游戏服务角色id", example = "1000100")
    private Long serverRoleId;

    @ApiModelProperty(value = "合约地址", example = "0xafag131jll........")
    private String contractAddress;

    @ApiModelProperty(value = "链", example = "Solana")
    private String chain;

    @ApiModelProperty(value = "token id", example = "256")
    private String tokenId;

    @ApiModelProperty(value = "协议标准", example = "xxxx")
    private String tokenStandard;

    @ApiModelProperty(value = "是否分配。0未分配 1已分配", example = "0")
    private Integer isConfiguration;

    @ApiModelProperty(value = "单据状态 1 转化中，2 转化成功，3 withdraw ", example = "2")
    private Integer state;

    @ApiModelProperty(value = "动态属性，存json", example = "{\"attr\":\"动态属性\"}")
    private String attributes;

    @ApiModelProperty("nft元数据，存json")
    private String metadata;
}
