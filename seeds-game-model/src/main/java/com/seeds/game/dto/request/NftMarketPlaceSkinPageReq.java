package com.seeds.game.dto.request;
import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 皮肤分页查询
 * @author: dengyang
 * @date 2023/2/22
 */
@Data
public class NftMarketPlaceSkinPageReq extends PageReq {

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("token id")
    private String tokenId;

    @ApiModelProperty("NFT状态：0：Buy Now  1：On Auction")
    private Integer status;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("耐久度")
    private Integer durability;

    @ApiModelProperty("稀有度：1，Common 2，Rare 3，Epic")
    private Integer rarity;

    @ApiModelProperty("NFT类型：1，皮肤  2，装备")
    private Integer type;

    @ApiModelProperty("NFT是否上架：0：未上架  1：已上架")
    private Integer isShelf;

    @ApiModelProperty("最小价格")
    private BigDecimal MinPrice;

    @ApiModelProperty("最大价格")
    private BigDecimal MaxPrice;

    @ApiModelProperty("点赞数量")
    private Integer favorite;

    @ApiModelProperty("英雄类型5种：1，DESTIN  2，AILITH  3，AILSA  4，NELA  5，CATHAL")
    private Integer heroType;

    @ApiModelProperty("胜率")
    private BigDecimal winRate;

    @ApiModelProperty("上架时间：24小时内  7天内  30天内")
    private String ListedTime;

}
