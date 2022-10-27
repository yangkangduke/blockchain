package com.seeds.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MgtWalletTransferQueryDto {

    private long startTime;
    private long endTime;
    private String fromAddress;
    private String toAddress;
    private int current;
    private int pageSize;
    private Integer status;
    private int chain;
}
