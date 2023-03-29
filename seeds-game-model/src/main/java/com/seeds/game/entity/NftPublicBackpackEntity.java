package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * NFT公共背包
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@TableName("ga_nft_public_backpack")
@ApiModel(value = "NftPublicBackpack对象", description = "NFT公共背包")
@Data
public class NftPublicBackpackEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    @TableField("`desc`")
    private String desc;

    @ApiModelProperty("icon")
    private String icon;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("类型：1装备 2道具 3英雄")
    private Integer type;

    @ApiModelProperty("对应游戏那边的conf_id，找静态表中的数据")
    private Long itemId;

    @ApiModelProperty("对应游戏那边的item_id")
    private Long itemTypeId;

    @ApiModelProperty("游戏那边传的。大区+game+流水")
    private Long autoId;

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

    @ApiModelProperty("version")
    private Long version;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("修改人")
    private Long updatedBy;

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;

    @ApiModelProperty("是否分配。0未分配 1已分配")
    private Integer isConfiguration;

    @ApiModelProperty("状态 1 MINTED (mint成功)，2 LOCK (作为合成材料被临时锁定)，3 DEPOSITED (托管给平台) 4 UNDEPOSITED (解除托管)  ")
    private Integer state;

    @ApiModelProperty("动态属性，存json")
    private String attributes;

    @ApiModelProperty("nft元数据，存json")
    private String metadata;

    @ApiModelProperty("nft价格")
    private BigDecimal price;

    @ApiModelProperty("nft等级")
    private Integer grade;

    private String tokenName;

    private String tokenAddress;

    @ApiModelProperty("关联z_equipment_nft表")
    private Long eqNftId;
}
