package com.seeds.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawTransactionDto {
    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private BigInteger value;
    private String data;
    private BigInteger gasPremium;
    private BigInteger feeCap;
    // 当RawTransactionDto 被签名发上链后，使用者可以更新txnHash到该字段
    private String txnHash;

    /**
     * TRX transaction
     */
    String tronTransactionExtensionBase64;

    /**
     * TRC20 transaction
     */
    String tronTransactionBase64;
}
