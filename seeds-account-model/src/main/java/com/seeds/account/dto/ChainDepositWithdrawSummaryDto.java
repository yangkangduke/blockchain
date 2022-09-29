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
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainDepositWithdrawSummaryDto implements Serializable {
    private static final long serialVersionUID = -1L;

    long userId;
    long firstDepositTime;
    long lastDepositTime;
    long firstWithdrawTime;
    long lastWithdrawTime;

    BigDecimal totalDepositValue;
    BigDecimal totalWithdrawValue;
    BigDecimal totalNetValue;
}
