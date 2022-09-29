package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author guocheng
 * @date 2021/1/7
 *
 * 链上兑换 response dto
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainExchangeDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private BigDecimal amountOut;

    private String txHash;
}
