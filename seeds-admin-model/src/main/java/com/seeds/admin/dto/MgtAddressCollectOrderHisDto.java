package com.seeds.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MgtAddressCollectOrderHisDto {

    long id;
    long createTime;
    long updateTime;

    String currency;
    String address;

    String amount;
    String feeAmount;
    int status;

}
