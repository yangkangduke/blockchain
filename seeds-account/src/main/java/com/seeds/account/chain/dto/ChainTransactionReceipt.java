package com.seeds.account.chain.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChainTransactionReceipt {

    private Boolean status;
    private Long blockNumber;
    private String blockHash;
    private Long gasUsed;
    private List<Log> logs;
    private String revertReason;
}
