package com.seeds.account.model;

import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 归集历史
 *
 * @author yk
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressCollectHis {

    private long id;
    private long createTime;
    private long updateTime;
    private long version;

    private long orderId;
    private String fromAddress;
    private String toAddress;
    private String currency;
    private BigDecimal amount;
    private long gasPrice;
    private long gasLimit;
    private BigDecimal txFee;
    private long blockNumber;
    private String blockHash;
    private String txHash;
    private String nonce;
    private String txValue;

    private String comments;
    private int status;

    private String txToAddress;

    private Chain chain;
}
