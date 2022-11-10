package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRuleDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;
    /**
     * @see com.seeds.common.enums.Chain
     */
    @ApiModelProperty(value = "1：ETH 3：TRON")
    Integer chain;

    @ApiModelProperty(value = "币种")
    String currency;

    @ApiModelProperty(value = "手续费类型")
    int feeType;

    @ApiModelProperty(value = "手续费")
    BigDecimal feeAmount;

    @ApiModelProperty(value = "金额精度")
    private Integer decimals;

    @ApiModelProperty(value = "提币状态 1启用 2停用")
    private Integer status;

    @ApiModelProperty("min amount")
    private BigDecimal minAmount;

    @ApiModelProperty("max amount")
    private BigDecimal maxAmount;

    @ApiModelProperty("intrady amount")
    private BigDecimal intradayAmount;

    @ApiModelProperty("auto withdraw amount")
    private BigDecimal autoAmount;

    @ApiModelProperty("是否内部提币面手续费  false、true")
    private String zeroFeeOnInternal;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;
}
