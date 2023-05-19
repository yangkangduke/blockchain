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

    @ApiModelProperty("交易签名")
    @NotBlank(message = "Sig cannot be empty")
    private String sig;

    @ApiModelProperty("交易地址")
    private String tokenAddress;

    @ApiModelProperty("nft地址")
    private String mintAddress;

    @ApiModelProperty("tokenId")
    private String name;

    @ApiModelProperty("用户地址")
    private String owner;

    @ApiModelProperty("type 1 装备 2 道具 3英雄")
    private Integer type;
}
