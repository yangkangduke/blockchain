package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "SysNftSoldOutReq", description = "NFT下架请求入参")
public class SysNftSoldOutReq {

    @ApiModelProperty(value = "NFT唯一标识")
    @NotNull(message = "The id cannot be empty")
    private Long nftId;
}
