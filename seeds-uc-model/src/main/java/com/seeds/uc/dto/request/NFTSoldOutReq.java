package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "SysNftSoldOutReq", description = "NFT下架请求入参")
public class NFTSoldOutReq {

    @ApiModelProperty(value = "NFT唯一标识")
    @NotNull(message = "The id cannot be empty")
    private Long nftId;

    @ApiModelProperty(value = "NFT的归属人id")
    private Long userId;

}
