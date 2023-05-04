package com.seeds.game.dto.request;
import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 皮肤分页查询
 * @author: dengyang
 * @date 2023/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "NftMarketPlaceSkinPageReq")
public class NftMarketPlaceSkinPageReq extends PageReq {

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("token id")
    private String tokenId;

    @ApiModelProperty("由：auction_id判断：0：一口价  大于0：On Auction ")
    private Long auctionId;

    @ApiModelProperty("稀有度：1，Common 2，Rare 3，Epic")
    private Integer rarity;

    @ApiModelProperty("最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty("最大价格")
    private BigDecimal maxPrice;

    @ApiModelProperty("英雄类型5种：1，DESTIN  2，AILITH  3，AILSA  4，NELA  5，CATHAL")
    private Integer heroType;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序类型 0 :升序 1:降序")
    private Integer  sortType;

    @ApiModelProperty(value = "排序类型 asc:升序 desc:降序")
    private String sortTypeStr;


    public static String convert(Integer sortType) {
        String str = "desc";
        if (sortType.equals(0)) {
            str = "asc";
        }
        return str;
    }
}
