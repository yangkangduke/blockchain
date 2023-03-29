package com.seeds.game.dto.request.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author hang.yu
 * @date 2023/3/28
 */
@Data
public class EndAuctionMessageDto {

    @ApiModelProperty("拍卖id")
    private Long auctionId;

    @ApiModelProperty("出价id")
    private Long bidingId;

    @ApiModelProperty("nft地址")
    private String mintAddress;

    @ApiModelProperty("nft id: 装备表Id")
    private Long nftId;

    @ApiModelProperty("随机码")
    private String nonce;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("接收者地址")
    private String toAddress;

}
