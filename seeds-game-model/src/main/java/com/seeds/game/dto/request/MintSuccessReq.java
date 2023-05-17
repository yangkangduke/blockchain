package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: hewei
 * @date 2023/4/3
 */
@Data
public class MintSuccessReq {

    @NotNull
    @ApiModelProperty("事件id")
    private String eventId;

    @NotNull
    @ApiModelProperty("nft id")
    private Long id;

    @ApiModelProperty("token id")
    @NotNull
    private Long tokenId;

    private String name;

    @ApiModelProperty("NFT address")
    @NotBlank
    private String mintAddress;

    @ApiModelProperty("NFT拥有者地址")
    @NotBlank
    private String owner;

}
