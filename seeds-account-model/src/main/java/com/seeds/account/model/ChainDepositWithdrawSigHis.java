package com.seeds.account.model;

import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainDepositWithdrawSigHis {

    private Long id;
    private Long createTime;
    private Long updateTime;
    private Long version;

    private Long userId;
    private Chain chain;
    private String currency;
    private BigDecimal amount;
    private long deadline;

    /**
     * 要签名消息的签名
     */
    private String signedSignature;
    /**
     * 要签名消息
     */
    private String signedMessage;
}