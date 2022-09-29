package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainExchangeExecutionDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @NotNull(message = "sourceCurrency shouldn't be null")
    private String sourceCurrency;

    @NotNull(message = "sourceCurrency shouldn't be null")
    private String targetCurrency;

    @Positive(message = "sourceAmount should be greater than 0")
    private BigDecimal sourceAmount;

    private BigDecimal targetAmount;

    /**
     * 1-分红地址兑换，2-兑换账户兑换
     */
    private int action;

    /**
     * 最低接受换得
     */
    private BigDecimal minAmountOut;

    /**
     * 最长成交时间 in millis
     */
    private Long chainDeadline;

    /**
     * 1-ETH, 2-BSC, 3-TRON
     */
    private int chain;

    @JsonIgnore
    private Long operationId;
}
