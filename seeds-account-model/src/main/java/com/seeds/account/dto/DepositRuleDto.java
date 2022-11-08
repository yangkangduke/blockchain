package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.CommonStatus;
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
public class DepositRuleDto implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    /**
     * @see com.seeds.common.enums.Chain
     */
    @ApiModelProperty(value = "1：ETH 3：TRON")
    int chain;

    /**
     * 币种
     */
    @ApiModelProperty(value = "币种")
    String currency;

    /**
     * 免审核
     */
    @ApiModelProperty(value = "免审核数量")
    BigDecimal autoAmount;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用 1：启用 2：停用")
    CommonStatus status;


    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("version")
    private Long version;

    @ApiModelProperty("最小提币金额")
    private BigDecimal minAmount;

    @ApiModelProperty("精度")
    private Integer decimals;
}
