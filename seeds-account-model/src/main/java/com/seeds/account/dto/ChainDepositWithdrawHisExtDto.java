package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.ChainAction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充提币扩展
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class ChainDepositWithdrawHisExtDto implements Serializable {
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
    String comments;
    int blacklist;
    int status;
    String txHash;

    int chain;
    boolean isWithinDeadline;
}
