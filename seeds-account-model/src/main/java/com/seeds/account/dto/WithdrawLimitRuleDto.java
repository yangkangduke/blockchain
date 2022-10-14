package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    /**
     * 币种
     */
    String currency;
    /**
     * 最小
     */
    BigDecimal minAmount;

    /**
     * 最大
     */
    BigDecimal maxAmount;

    /**
     * 当日限额
     */
    BigDecimal intradayAmount;

    /**
     * 免审核提币
     */
    BigDecimal autoAmount;

    /**
     * 是否内部提币免手续费
     */
    String zeroFeeOnInternal;
}
