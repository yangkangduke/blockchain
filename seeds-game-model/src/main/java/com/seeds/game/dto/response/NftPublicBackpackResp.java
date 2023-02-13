package com.seeds.game.dto.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jnr.ffi.annotations.In;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hewei
 * @since 2023-01-31
 */
@ApiModel(value = "NftPublicBackpackResp")
@Data
public class NftPublicBackpackResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("icon")
    private String icon;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("类型：1装备 2道具 3英雄")
    private Integer type;

    @ApiModelProperty("对应游戏那边的conf_id，找静态表中的数据")
    private Long itemId;

    @ApiModelProperty("游戏那边传的。大区+game+流水")
    private Integer autoId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("游戏服务角色id")
    private Long serverRoleId;

    @ApiModelProperty("合约地址")
    private String contractAddress;

    @ApiModelProperty("链")
    private String chain;

    @ApiModelProperty("token id")
    private String tokenId;

    @ApiModelProperty("协议标准")
    private String tokenStandard;

    @ApiModelProperty("是否分配。0未分配 1已分配")
    private Integer isConfiguration;

    @ApiModelProperty("单据状态 1 转化中，2 转化成功，3 withdraw  ")
    private Integer state;

    @ApiModelProperty("动态属性，存json")
    private String attributes;

    @ApiModelProperty("nft元数据，存json")
    private String metadata;
}