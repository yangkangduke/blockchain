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
public class WithdrawLimitRuleDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "最小")
    private BigDecimal minAmount;


    @ApiModelProperty(value = "最大")
    private BigDecimal maxAmount;


    @ApiModelProperty(value = "当日限额")
    private BigDecimal intradayAmount;


    @ApiModelProperty(value = "免审核提币")
    private BigDecimal autoAmount;


    @ApiModelProperty(value = "是否内部提币免手续费")
    private String zeroFeeOnInternal;

    @ApiModelProperty(value = "createTime")
    private Long createTime;

    @ApiModelProperty(value = "updateTime")
    private Long updateTime;


}
