package com.seeds.game.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 市场皮肤分页查询返回列表
 * @author dengyang
 * @since 2023-03-21
 */
@ApiModel(value = "NftMarketPlaceSkinResp")
@Data
public class NftMarketPlaceSkinResp implements Serializable {
     private static final long serialVersionUID = 1L;

     @ApiModelProperty("nft id")
     @JsonSerialize(using= ToStringSerializer.class)
     private Long nftId;

     @ApiModelProperty("NFT编号")
     private String number;

     @ApiModelProperty("NFT名称")
     private String name;

     @ApiModelProperty("tokenId")
     private String tokenId;

     @ApiModelProperty("稀有度：1，Normal 2，Rare 3，Epic")
     private String rarity;

     @ApiModelProperty("NFT图片")
     private String image;

     @ApiModelProperty("NFT状态：0：UnDeposited  1：Deposited 2:On shelf 3:On auction 4:In settlement 5: Burned")
     private Integer state;

     @ApiModelProperty("拍卖NFT(setting id)")
     @JsonSerialize(using= ToStringSerializer.class)
     private Long auctionId;

     @ApiModelProperty("NFT最近历史交易价格")
     private BigDecimal lastSale;

     @ApiModelProperty("NFT价格")
     private BigDecimal price;

     @ApiModelProperty("上架时间")
     private Long placeTime;

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

     @ApiModelProperty("是否是拥有者，0 否 1 是")
     private Integer isOwner = 0;

}
