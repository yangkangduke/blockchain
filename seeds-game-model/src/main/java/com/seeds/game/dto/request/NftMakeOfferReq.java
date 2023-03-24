package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hang.yu
 * @date 2023/3/24
 */
@Data
public class NftMakeOfferReq {

    @ApiModelProperty("拍卖id")
    @NotBlank(message = "Auction Id cannot be empty")
    private String auctionId;

    @ApiModelProperty("购买收据")
    @NotBlank(message = "Receipt cannot be empty")
    private String receipt;

    @ApiModelProperty("交易地址")
    @NotBlank(message = "Sig key cannot be empty")
    private String sig;

}
