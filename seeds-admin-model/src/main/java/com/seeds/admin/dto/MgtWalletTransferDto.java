package com.seeds.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MgtWalletTransferDto {
    private long id;
    private long createTime;
    private long updateTime;
    private long version;
    private String fromAddress;
    private String toAddress;
    private String currency;
    private String amount;
    private String gasPrice;
    private long gasLimit;
    private String txFee;
    private long blockNumber;
    private String blockHash;
    private String txHash;
    private int status;
    private String comments;
    private String nonce;
}
