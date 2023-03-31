package com.seeds.game.dto.request;
import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 道具分页查询
 * @author: dengyang
 * @date 2023/2/22
 */
@Data
public class NftMarketPlacePropsPageReq {
    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("token id")
    private String tokenId;

    @ApiModelProperty("交易模式：1，Buy Now   2，On Auction")
    private Integer status;

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
