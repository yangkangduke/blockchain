package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.CommonStatus;
import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author milo
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainExchangeRuleDto implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * 源币种
     */
    String sourceCurrency;
    /**
     * 目标币种
     */
    String targetCurrency;

    Chain chain;

    Long chainDeadline;

    BigDecimal toleranceRate;

    Boolean isPrefer;

    Boolean isRoute;
    /**
     * 状态
     */
    CommonStatus status;
}
