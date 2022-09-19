package com.seeds.wallet.dto;

import lombok.Data;

@Data
public class RawTransactionSignRequest {
    private int chain;
    private long chainId;

    private RawTransactionDto rawTransaction;
    private String fromAddress;
}
