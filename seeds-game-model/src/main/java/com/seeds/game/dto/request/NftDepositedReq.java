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
public class NftDepositedReq {

    @ApiModelProperty("Nft id")
    @NotNull(message = "Nft id cannot be empty")
    private Long nftId;

    @ApiModelProperty("交易签名")
    @NotBlank(message = "Sig cannot be empty")
    private String sig;

    @ApiModelProperty("交易地址")
    @NotBlank(message = "Token address cannot be empty")
    private String tokenAddress;

}
