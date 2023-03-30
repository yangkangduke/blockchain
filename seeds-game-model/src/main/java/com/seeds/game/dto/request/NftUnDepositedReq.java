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
public class NftUnDepositedReq {

    @ApiModelProperty("Nft id")
    @NotNull(message = "Nft id cannot be empty")
    private Long nftId;

    @ApiModelProperty("随机码")
    @NotBlank(message = "Nonce cannot be empty")
    private String nonce;

    @ApiModelProperty("签名")
    @NotBlank(message = "signature cannot be empty")
    private String signature;

    @ApiModelProperty("接收者地址")
    @NotBlank(message = "To address cannot be empty")
    private String toAddress;

}
