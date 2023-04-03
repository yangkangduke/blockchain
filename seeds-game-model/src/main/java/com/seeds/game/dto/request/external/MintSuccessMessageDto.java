package com.seeds.game.dto.request.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/30
 */
@Data
public class MintSuccessMessageDto {

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
}
