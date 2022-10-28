package com.seeds.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MgtChainTypeDto {

    @ApiModelProperty(value = "chain code", example = "1")
    private int code;
    @ApiModelProperty(value = "chain name", example = "ETH")
    private String name;
    @ApiModelProperty(value = "原生代币", example = "ETH")
    private String nativeToken;
    @ApiModelProperty(value = "原生代币的精度", example = "18")
    private int decimals;
}
