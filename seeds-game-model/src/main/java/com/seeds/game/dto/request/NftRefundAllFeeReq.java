package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author hang.yu
 * @since 2023-03-24
 */
@Data
public class NftRefundAllFeeReq {

    @ApiModelProperty("托管费hash")
    @NotBlank(message = "Fee hash cannot be empty")
    private String feeHash;

    @ApiModelProperty("NFT address")
    @NotBlank(message = "Mint address cannot be empty")
    private String mintAddress;

}
