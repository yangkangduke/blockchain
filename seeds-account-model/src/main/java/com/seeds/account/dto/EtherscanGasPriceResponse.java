package com.seeds.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtherscanGasPriceResponse {
    private String status;
    private String message;
    private EtherscanGasPriceResult result;
}
