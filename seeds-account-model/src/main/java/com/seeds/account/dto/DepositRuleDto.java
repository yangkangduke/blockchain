package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "DepositRuleDto", description = "充币规则")
public class DepositRuleDto implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    /**
     * @see com.seeds.common.enums.Chain
     */
    @ApiModelProperty(value = "1：ETH 3：TRON")
    private int chain;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "免审核金额")
    private BigDecimal autoAmount;

    @ApiModelProperty(value = "是否启用 1：启用 2：停用")
    private int status;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("最小提币金额")
    private BigDecimal minAmount;

    @ApiModelProperty("精度")
    private Integer decimals;
}
