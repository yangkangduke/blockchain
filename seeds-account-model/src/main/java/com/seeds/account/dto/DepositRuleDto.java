package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.CommonStatus;
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

    /**
     * @see com.seeds.common.enums.Chain
     */
    int chain;

    /**
     * 币种
     */
    String currency;

    /**
     * 免审核
     */
    BigDecimal autoAmount;

    /**
     * 是否启用
     */
    CommonStatus status;
}
