package com.seeds.game.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hewei
 * @since 2023-01-31
 */
@ApiModel(value = "NftPublicBackpackSkinWebResp")
@Data
public class NftPublicBackpackSkinWebResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("nft物品名称")
    private String name;

    @ApiModelProperty("皮肤所属的英雄名字")
    private String heroName;

    @ApiModelProperty("图片（中心化地址）")
    private String image;

    @ApiModelProperty("shadow图片地址")
    private String imageSha;

    @ApiModelProperty("tokenId")
    private String tokenId;

    @ApiModelProperty("tokenAddress")
    private String tokenAddress;

    @ApiModelProperty("区服名字")
    private String serverName;

    @ApiModelProperty("稀有度：1，Normal 2，Rare 3，Epic")
    private Integer rarity;

    @ApiModelProperty("角色id")
    private Long serverRoleId;

    @ApiModelProperty("price")
    private BigDecimal price;

    @ApiModelProperty("状态 1 burn，2 LOCK (作为合成材料被临时锁定)，3 DEPOSITED (托管给平台) 4 UNDEPOSITED (解除托管)  5 on shelf 6 on auction 7 结算中")
    private Integer state;

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

    @ApiModelProperty("nftId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long eqNftId;
}
