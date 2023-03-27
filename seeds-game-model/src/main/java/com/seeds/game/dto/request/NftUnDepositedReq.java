package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hang.yu
 * @date 2023/3/24
 */
@Data
public class NftUnDepositedReq {

    @ApiModelProperty("nft地址")
    @NotBlank(message = "Mint Address cannot be empty")
    private String mintAddress;

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
