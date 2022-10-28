package com.seeds.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MgtChainTxnDto {

    @ApiModelProperty(value = "记录id")
    private Long id;

    @ApiModelProperty(value = "类型 1-提币，2-热钱包划转，3-资金归集，4-Gas划转，5-MCD上报，6-兑换，7-分红兑换，8-分红发放")
    private Integer type;

    @ApiModelProperty(value = "交易hash")
    private String txHash;

}
