package com.seeds.admin.dto.request.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hewei
 * @date 2023/5/5
 */
@Data
public class SkinNftEnglishDto {

    private String mintAddress;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("auctionHouseAddress")
    private String auctionHouseAddress;

    @ApiModelProperty("持续时间")
    private Integer duration;

    @ApiModelProperty("获取人： 0 highest bidder")
    private Integer bidder;

    @ApiModelProperty("固定价格")
    private BigDecimal startPrice;

    @ApiModelProperty("币种: sol")
    private String current;
}
