package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hang.yu
 * @date 2023/3/24
 */
@Data
public class NftDepositedReq {

    @ApiModelProperty("nft地址")
    @NotBlank(message = "Mint Address cannot be empty")
    private String mintAddress;

    @ApiModelProperty("交易签名")
    @NotBlank(message = "Sig cannot be empty")
    private String sig;

    @ApiModelProperty("交易地址")
    @NotBlank(message = "Token address cannot be empty")
    private String tokenAddress;

}
