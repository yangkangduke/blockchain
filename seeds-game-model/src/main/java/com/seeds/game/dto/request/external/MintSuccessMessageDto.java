package com.seeds.game.dto.request.external;

import com.seeds.game.dto.request.NftMintSuccessReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/30
 */
public class MintSuccessMessageDto {
    @ApiModelProperty(value = "是否自动托管 1 是 0 否")
    private Integer autoDeposite;

    @ApiModelProperty(value = "mint address")
    private String mintAddress;

    @ApiModelProperty(value = "name: tokenId")
    private String name;

    @ApiModelProperty(value = "用户地址")
    private String owner;

    @ApiModelProperty(value = "交易签名")
    private String sig;

    @ApiModelProperty(value = "token address")
    private String tokenAddress;

    @ApiModelProperty(value = "装备nft")
    private NftMintSuccessReq.Equipment zequipment;

    @Data
    public static class Equipment {

        @ApiModelProperty(value = "装备nft")
        private int createTime;

        @ApiModelProperty(value = "id")
        private int id;

        @ApiModelProperty(value = "nftId")
        private int nftId;

        @ApiModelProperty(value = "nftTokenId")
        private int nftTokenId;

        @ApiModelProperty(value = "ownerId")
        private String ownerId;
    }
}
