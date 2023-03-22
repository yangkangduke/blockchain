package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author hang.yu
 * @date 2023/03/22
 */

@Data
@ApiModel(value = "NftChainDetailResp")
public class NftChainDetailResp {

    @ApiModelProperty("合约地址")
    private String contractAddress;

    @ApiModelProperty("Token Id")
    private String tokenId;

    @ApiModelProperty("Token Standard")
    private String tokenStandard;

    @ApiModelProperty("Chain")
    private String chain;

    @ApiModelProperty("最后更改时间")
    private String lastUpdated;

    @ApiModelProperty("创造者收益")
    private String creatorEarnings;

}
