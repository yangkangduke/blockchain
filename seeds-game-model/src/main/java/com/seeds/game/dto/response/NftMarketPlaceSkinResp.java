package com.seeds.game.dto.response;
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

     @ApiModelProperty("NFT编号")
     private String number;

     @ApiModelProperty("tokenId")
     private String tokenId;

     @ApiModelProperty("稀有度：1，Common 2，Rare 3，Epic")
     private Integer rarity;

     @ApiModelProperty("NFT图片")
     private String image;

     @ApiModelProperty("NFT交易模式：0：Buy Now  1：On Auction")
     private Integer model;

     @ApiModelProperty("NFT价格")
     private BigDecimal price;

     @ApiModelProperty("上架时间")
     private Long ListTime;

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
