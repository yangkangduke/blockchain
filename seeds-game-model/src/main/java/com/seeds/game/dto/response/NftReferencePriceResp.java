package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author hang.yu
 * @date 2023/06/27
 */

@Data
@ApiModel(value = "NftReferencePriceResp")
public class NftReferencePriceResp {

    @ApiModelProperty("游戏itemId")
    private Long id;

    @ApiModelProperty("参考单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("修改时间")
    private Long updateTime;

}
