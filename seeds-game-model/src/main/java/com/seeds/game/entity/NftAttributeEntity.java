package com.seeds.game.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * NFT属性
 * </p>
 *
 * @author dengyang
 * @since 2023-03-22
 */
@TableName("ga_nft_attribute")
@ApiModel(value = "NftAttributeEntity对象", description = "NFT属性")
@Data
public class NftAttributeEntity extends BaseEntity {

    @ApiModelProperty("NFT类型：1，皮肤  2，装备")
    private Integer type;

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("NFT交易模式：0：Buy Now  1：On Auction")
    private Integer model;

    @ApiModelProperty("NFT价格")
    private BigDecimal price;

    @ApiModelProperty("耐久度")
    private Integer durability;

    @ApiModelProperty("稀有度：1，Common 2，Rare 3，Epic")
    private Integer rarity;

    @ApiModelProperty("英雄类型5种：1，DESTIN  2，AILITH  3，AILSA  4，NELA  5，CATHAL")
    private Integer heroType;

    @ApiModelProperty("浏览量")
    private Integer views;

    @ApiModelProperty("token id")
    private String tokenId;

    @ApiModelProperty("tokenAddress")
    private String tokenAddress;

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

    @ApiModelProperty("获胜次数")
    private Integer victory;

    @ApiModelProperty("失败次数")
    private Integer lose;

    @ApiModelProperty("最大连胜场数")
    private Integer maxStreak;

    @ApiModelProperty("击杀玩家数")
    private Integer capture;

    @ApiModelProperty("最大连杀数")
    private Integer killingSpree;

    @ApiModelProperty("击杀NPC数")
    private Integer goblinKill;

    @ApiModelProperty("被玩家击杀数")
    private Integer slaying;

    @ApiModelProperty("被NPC击杀数")
    private Integer goblin;

}