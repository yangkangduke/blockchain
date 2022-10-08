package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainTxnDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 1-提币，2-热钱包划转，3-资金归集，4-Gas划转，5-MCD上报，6-兑换，7-分红兑换，8-分红发放
     */
    private Integer type;

    private String fromAddress;

    private String toAddress;

    private String txHash;

    private BigInteger nonce;

    private BigInteger gasLimit;

    private BigInteger gasPrice;

    private BigInteger chainGasPrice;

    /**
     * 1-提交上链，2-链上执行完毕，待安全确认，3-链上确认成功，4-链上失败，5-链上取消，6-原tx取消，replace 成功
     */
    private Integer status;

    /**
     * txn 发起时间
     */
    private long createTime;

    /**
     * txn 安全确认时间
     */
    private long updateTime;

    private Long id;

    private Long blockNumber;

    /**
     * 前安全确认块数目（12块）的from address nonce
     */
    private BigInteger confirmedSafeNonce;

    /**
     * 1-ETH, 2-BSC, 3-TRON
     */
    private Integer chain;
}
