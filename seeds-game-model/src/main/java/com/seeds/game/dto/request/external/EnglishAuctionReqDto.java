package com.seeds.game.dto.request.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author hang.yu
 * @date 2023/3/28
 */
@Data
public class EnglishAuctionReqDto {

    @ApiModelProperty("nft拍卖地址")
    private String auctionHouseAddress;

    @ApiModelProperty("获取人： 0 highest bidder")
    private Integer bidder;

    @ApiModelProperty("币种: sol")
    private String current;

    @ApiModelProperty("持续时间")
    private Integer duration;

    @ApiModelProperty("nft地址")
    private String mintAddress;

    @ApiModelProperty("随机码")
    private String nonce;

    @ApiModelProperty("发起人地址")
    private String ownerAddress;

    @ApiModelProperty("上架拍卖签名")
    private String signature;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("开始价格")
    private BigDecimal startPrice;

}
