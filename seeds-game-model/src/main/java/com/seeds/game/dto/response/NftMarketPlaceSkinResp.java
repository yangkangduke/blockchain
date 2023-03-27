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

     @ApiModelProperty("主键")
     private Long id;

     @ApiModelProperty("NFT编号")
     private String number;

     @ApiModelProperty("NFT图片")
     private String image;

     @ApiModelProperty("NFT名称")
     private String name;

     @ApiModelProperty("NFT价格")
     private BigDecimal price;

     @ApiModelProperty("NFT等级")
     private Integer grade;

     @ApiModelProperty("耐久度")
     private Integer durability;

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
