package com.seeds.game.dto.request;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

    @ApiModelProperty("由：auction_id判断：0：一口价  大于0：On Auction ")
    private Long auctionId;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("最小耐久度")
    private Integer minDurability;

    @ApiModelProperty("最大耐久度")
    private Integer maxDurability;

    @ApiModelProperty("最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty("最大价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序类型 0:升序 1:降序")
    private Integer sortType;

    private String sortTypeStr;

    public static String convert(Integer sortType) {
        String str = "desc";
        if (sortType.equals(0)) {
            str = "asc";
        }
        return str;
    }

}
