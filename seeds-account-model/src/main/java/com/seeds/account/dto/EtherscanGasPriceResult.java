package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtherscanGasPriceResult {
    @JsonProperty("LastBlock")
    private String lastBlock;
    @JsonProperty("SafeGasPrice")
    private String safeGasPrice;
    @JsonProperty("ProposeGasPrice")
    private String proposeGasPrice;
    @JsonProperty("FastGasPrice")
    private String fastGasPrice;
}
