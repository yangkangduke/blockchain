package com.seeds.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MgtAddressCollectOrderRequestDto {

    private Long id;

    /**
     * @see com.seeds.account.enums.FundCollectOrderType
     */
    int type;

    int chain;
    String currency;
    String address;
    String gasPrice;
    List<MgtAddressOrderDetail> list = Lists.newLinkedList();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MgtAddressOrderDetail {
        String address;
        String amount;
    }
}
