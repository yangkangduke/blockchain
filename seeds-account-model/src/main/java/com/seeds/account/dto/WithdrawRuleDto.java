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
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRuleDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     *
     * @see com.seeds.common.enums.Chain
     */
    Chain chain;
    /**
     * 币种
     */
    String currency;

    /**
     * 手续费类型
     */
    int feeType;

    /**
     * 手续费
     */
    BigDecimal feeAmount;

    /**
     * 金额精度
     */
    int decimals;

    /**
     * 提币状态
     */
    CommonStatus status;
}
