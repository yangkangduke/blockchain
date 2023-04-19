package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hang.yu
 * @date 2023/3/24
 */
@Data
public class NftFixedPriceShelfReq {

    @ApiModelProperty("Nft id")
    @NotNull(message = "Nft id cannot be empty")
    private Long nftId;

    @ApiModelProperty("购买收据")
    @NotBlank(message = "Receipt cannot be empty")
    private String receipt;

    @ApiModelProperty("交易地址")
    @NotBlank(message = "Sig key cannot be empty")
    private String sig;

    @ApiModelProperty("托管费交易hash")
    @NotBlank(message = "Fee hash cannot be empty")
    private String feeHash;

}
