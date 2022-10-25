package com.seeds.admin.dto;

import com.seeds.account.enums.ChainAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MgtDepositWithdrawDto {
    long id;
    long createTime;
    long updateTime;
    long userId;
    int internal;
    String fromAddress;
    String toAddress;
    String currency;
    ChainAction action;
    String amount;
    String feeAmount;
    int status;
    String statusDesc;
    int blacklist;
    /**
     * 审核comments
     */
    String comments;

    Integer chainCode;

    String chainName;

    /**
     * 交易Hash
     */
    String txHash;
}
