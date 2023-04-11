package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * SOL对美元转换
 *
 * @author hang.yu
 * @date 2023/03/30
 */
@Data
@ApiModel(value = "ServerRoleResp")
public class SolToUsdRateResp {

    @ApiModelProperty("汇率")
    private BigDecimal priceUsdt;

    @ApiModelProperty("卷轴")
    private BigDecimal volumeUsdt;

    @ApiModelProperty("市价指数")
    private BigDecimal marketCapFD;

    @ApiModelProperty("市场占有率排名")
    private BigDecimal marketCapRank;

    @ApiModelProperty("价格变化24h")
    private BigDecimal priceChange24h;

}
