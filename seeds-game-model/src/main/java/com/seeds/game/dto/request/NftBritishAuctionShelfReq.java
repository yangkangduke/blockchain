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

    @ApiModelProperty("Nft id")
    @NotNull(message = "Nft id cannot be empty")
    private Long nftId;

    @ApiModelProperty("随机码")
    @NotBlank(message = "Nonce Address cannot be empty")
    private String nonce;

    @ApiModelProperty("上架拍卖签名")
    @NotBlank(message = "Signature Address cannot be empty")
    private String signature;

    @ApiModelProperty("开始时间")
    @NotNull(message = "Start time cannot be empty")
    private Long startTime;

    @ApiModelProperty("开始价格")
    @NotNull(message = "Start price cannot be empty")
    private BigDecimal startPrice;

    @ApiModelProperty("托管费交易hash")
    @NotBlank(message = "FeeHash cannot be empty")
    private String feeHash;

}
