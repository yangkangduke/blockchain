package com.seeds.account.dto;

import com.seeds.account.enums.ExchangeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainStakeDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 质押市值
     */
    private Long stakeValue;

    /**
     * 系统质押率
     */
    private Long stakeRate;

    private Long version;

    private Long txId;

    private Long clientId;

    private Long systemId;

    private String sourceCurrency;

    private BigDecimal sourceAmount;

    private String targetCurrency;

    private BigDecimal targetAmount;

    private ExchangeStatus status;
}