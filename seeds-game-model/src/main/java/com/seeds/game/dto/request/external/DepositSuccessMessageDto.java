package com.seeds.game.dto.request.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author hang.yu
 * @date 2023/3/28
 */
@Data
public class DepositSuccessMessageDto {

    @ApiModelProperty("nft地址")
    private String mintAddress;

    @ApiModelProperty("tokenId")
    private String name;

    @ApiModelProperty("用户地址")
    private String owner;

    @ApiModelProperty("交易签名")
    private String sig;

    @ApiModelProperty("tokenAddress")
    private String tokenAddress;

    @ApiModelProperty("type 1 装备 2 道具 3英雄")
    private Integer type;

}
