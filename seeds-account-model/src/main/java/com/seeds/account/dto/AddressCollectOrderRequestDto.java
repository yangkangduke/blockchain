package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.seeds.account.enums.FundCollectOrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 1. 从多个用户地址转入热钱包
 * 2. 从热钱包给用户地址打ETH
 *
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressCollectOrderRequestDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     *
     * @see FundCollectOrderType
     */
    int chain;
    int type;
    String currency;
    String address;
    long gasPrice;
    List<AddressOrderDetail> list = Lists.newLinkedList();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressOrderDetail implements Serializable {
        String address;
        BigDecimal amount;
    }
}

