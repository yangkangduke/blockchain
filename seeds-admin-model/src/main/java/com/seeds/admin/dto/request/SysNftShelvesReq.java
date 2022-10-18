package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@ApiModel(value = "SysNftShelvesReq", description = "NFT上架请求入参")
public class SysNftShelvesReq {

    @ApiModelProperty(value = "NFT唯一标识")
    @NotNull(message = "The id cannot be empty")
    private Long nftId;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "NFT的归属人id")
    private Long userId;

}
