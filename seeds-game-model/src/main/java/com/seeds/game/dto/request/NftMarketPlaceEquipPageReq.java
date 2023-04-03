package com.seeds.game.dto.request;
import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 装备分页查询
 * @author: dengyang
 * @date 2023/2/22
 */
@Data
@ApiModel(value = "NftMarketPlaceEquipPageReq")
public class NftMarketPlaceEquipPageReq extends PageReq {

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("token id")
    private String tokenId;

    @ApiModelProperty("由：auction_id判断：1，0：一口价  2，大于0：On Auction ")
    private Integer auctionId;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("耐久度")
    private Integer durability;

    @ApiModelProperty("最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty("最大价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序类型 asc:升序 desc:降序")
    private String sortType;

}
