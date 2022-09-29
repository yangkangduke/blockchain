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
 * 链上分红 分红地址 -> minter合约划转response dto
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainDividendDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private String fromCurrency;

    private String toCurrency;

    private String fromAddress;

    private String toAddress;

    private BigDecimal fromAmount;

    private BigDecimal toAmount;

    private Long gasPrice;

    private Long gasLimit;

    private BigDecimal txFee;

    private Long blockNumber;

    private String blockHash;

    private String txHash;
}
