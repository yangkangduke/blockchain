package com.seeds.game.dto.request.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author hang.yu
 * @date 2023/3/28
 */
@Data
public class TransferSolMessageDto {

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("退还用户地址")
    private String toAddress;

}
