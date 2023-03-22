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
     private String id;

     @ApiModelProperty("NFT编号")
     private String identifier;

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
     private Integer wins;

     @ApiModelProperty("失败次数")
     private Integer failures;

     private Integer ties;

     @ApiModelProperty("连胜次数")
     private Integer winningStreak;

     private Integer consecutiveLosses;

     private Integer playerKills;

     private Integer maximumKills;

     private Integer npcKills;

     private Integer killedByAnother;

     private Integer killedByNpc;
}
