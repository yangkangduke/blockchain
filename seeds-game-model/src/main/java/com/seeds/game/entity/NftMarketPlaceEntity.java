package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * NFT市场
 * </p>
 *
 * @author dengyang
 * @since 2023-03-22
 */
@TableName("ga_nft_marketplace")
@ApiModel(value = "NftMarketPlaceEntity对象", description = "NFT市场")
@Data
public class NftMarketPlaceEntity extends BaseEntity {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("NFT编号")
    private Integer identifier;

    @ApiModelProperty("NFT名称")
    private Integer name;

    @ApiModelProperty("NFT价格")
    private BigDecimal price;

    @ApiModelProperty("NFT状态：0：Buy Now  1：On Auction")
    private Integer status;

    @ApiModelProperty("NFT是否上架：0：未上架  1：已上架")
    private Integer isShelf;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("耐久度")
    private Integer durability;

    @ApiModelProperty("稀有度：1，Common 2，Rare 3，Epic")
    private Integer rarity;

    @ApiModelProperty("英雄类型5种：1，DESTIN  2，AILITH  3，AILSA  4，NELA  5，CATHAL")
    private Integer heroType;

    @ApiModelProperty("点赞数量")
    private Integer favorite;

    @ApiModelProperty("浏览量")
    private Integer views;

    @ApiModelProperty("胜率")
    private Integer winRate;

    @ApiModelProperty("NFT等级")
    private Integer level;

    @ApiModelProperty("健康属性值")
    private Integer health;

    @ApiModelProperty("装备属性值")
    private Integer armor;

    @ApiModelProperty("怪物属性值")
    private Integer vsMonster;

    @ApiModelProperty("属性综合介绍")
    private String passive;

    @ApiModelProperty("blade属性综合介绍")
    private String bladeBuff;
}
