package com.seeds.account.chain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class NativeChainTransactionDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * @see Chain
     */
    Chain chain;
    long blockNumber;
    String blockHash;
    String txHash;
    long txTime;
    BigDecimal txFee;
    String fromAddress;
    String toAddress;
    String currency;
    BigDecimal amount;
}
