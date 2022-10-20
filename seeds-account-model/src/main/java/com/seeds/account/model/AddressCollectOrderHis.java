package com.seeds.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 1. 从多个用户地址转入热钱包
 * 2. 从热钱包给用户地址打ETH
 *
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressCollectOrderHis implements Serializable {
    private static final long serialVersionUID = -1L;

    long id;
    long createTime;
    long updateTime;
    long version;
    /**
     *
     * @see com.seeds.account.enums.FundCollectOrderType
     */
    int type;
    String currency;
    String address;
    BigDecimal amount;
    long gasPrice;
    BigDecimal feeAmount;
    /**
     *
     * @see com.seeds.account.enums.FundCollectOrderStatus
     */
    int status;

    Chain chain;
}

