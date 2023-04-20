package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author hang.yu
 * @since 2023-03-24
 */
@Data
public class NftRefundFeeReq {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("拍卖id")
    private Long auctionId;

}
