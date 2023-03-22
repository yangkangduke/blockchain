package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author hang.yu
 * @date 2023/03/22
 */

@Data
@ApiModel(value = "NftOfferResp")
public class NftOfferResp {

    @ApiModelProperty("出价价格")
    private BigDecimal price;

    @ApiModelProperty("美元价格")
    private String usdPrice;

    @ApiModelProperty("价格差异")
    private String floorDifference;

    @ApiModelProperty("到期时间")
    private String expiration;

    @ApiModelProperty("出价人")
    private String from;

}
