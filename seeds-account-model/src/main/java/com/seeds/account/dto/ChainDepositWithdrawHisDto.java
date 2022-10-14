package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.ChainAction;
import com.seeds.common.enums.Chain;
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
public class ChainDepositWithdrawHisDto implements Serializable {
    private static final long serialVersionUID = -1L;

    long id;
    long createTime;
    long updateTime;
    long userId;
    int internal;
    String fromAddress;
    String toAddress;
    String currency;
    ChainAction action;
    BigDecimal amount;
    BigDecimal feeAmount;
    /**
     * 审核comments
     */
    String comments;
    /**
     * 是否是黑名单地址 0不是 1是
     */
    int blacklist;
    int status;

    Chain chain;
    /**
     * 交易Hash
     */
    String txHash;
}
