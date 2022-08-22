package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "ChainMintNftResponse", description = "系统Mint NFT信息")
public class ChainMintNftResp {
    @ApiModelProperty(value = "smart contract address")
    private String contractAddress;

    @ApiModelProperty(value = "nft token id")
    private String tokenId;

    @ApiModelProperty(value = "nft token standard")
    private String tokenStandard;

    @ApiModelProperty(value = "blockchain nft is on")
    private String blockchain;

    @ApiModelProperty(value = "metadata location mode")
    private String metadata;

    @ApiModelProperty(value = "creator fee for nft")
    private BigDecimal creatorFees;
}
