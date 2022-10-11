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
 * 提币白名单
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawWhitelistDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long userId;
    private String currency;
    private BigDecimal maxAmount;
    private BigDecimal intradayAmount;
    private BigDecimal autoAmount;
    private String comments;
    private CommonStatus status;
}
