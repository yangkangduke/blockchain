package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author milo
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressCollectHisDto implements Serializable {
    private static final long serialVersionUID = -1L;

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

    private String comments;
    private int status;

    /**
     * 2021-04-30 milo
     */
    private Chain chain;
}
