package com.seeds.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotWallet {
    private Long id;
    private String address;
    private String fileJson;
    private Long createdAt;
    /**
     * 2021-04-23 milo 钱包支持多链
     */
    private int chain;
}