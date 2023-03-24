package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author hang.yu
 * @date 2023/3/24
 */
@Data
public class NftBritishAuctionShelfReq {

    @ApiModelProperty("nft拍卖地址")
    @NotBlank(message = "Auction House Address cannot be empty")
    private String auctionHouseAddress;

    @ApiModelProperty("获取人： 0 highest bidder")
    @NotNull(message = "Bidder cannot be empty")
    private Integer bidder;

    @ApiModelProperty("币种: sol")
    @NotBlank(message = "current cannot be empty")
    private String current;

    @ApiModelProperty("持续时间")
    @NotNull(message = "Duration cannot be empty")
    private Integer duration;

    @ApiModelProperty("nft地址")
    @NotBlank(message = "Mint Address cannot be empty")
    private String mintAddress;

    @ApiModelProperty("开始时间")
    @NotBlank(message = "Start time cannot be empty")
    private String start;

    @ApiModelProperty("开始价格")
    @NotNull(message = "Start price cannot be empty")
    private BigDecimal startPrice;

}
