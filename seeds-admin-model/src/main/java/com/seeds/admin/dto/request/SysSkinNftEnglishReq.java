package com.seeds.admin.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hewei
 * @date 2023/4/26
 */
@Data
@ApiModel(value = "SysSkinNftEnglishReq")
public class SysSkinNftEnglishReq {

    @ApiModelProperty("勾选条目的id集合")
    private List<Long> ids;

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
