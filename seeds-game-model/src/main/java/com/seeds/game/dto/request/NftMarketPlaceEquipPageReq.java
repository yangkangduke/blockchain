package com.seeds.game.dto.request;
import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 装备分页查询
 * @author: dengyang
 * @date 2023/2/22
 */
@Data
public class NftMarketPlaceEquipPageReq extends PageReq {

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("token id")
    private String tokenId;

    @ApiModelProperty("NFT交易模式：0：Buy Now  1：On Auction")
    private Integer model;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("耐久度")
    private Integer durability;

    @ApiModelProperty("稀有度：1，Common 2，Rare 3，Epic")
    private Integer rarity;

    @ApiModelProperty("NFT类型：1，皮肤  2，装备")
    private Integer type;

    @ApiModelProperty("最小价格")
    private BigDecimal MinPrice;

    @ApiModelProperty("最大价格")
    private BigDecimal MaxPrice;

    @ApiModelProperty("英雄类型5种：1，DESTIN  2，AILITH  3，AILSA  4，NELA  5，CATHAL")
    private Integer heroType;

}
