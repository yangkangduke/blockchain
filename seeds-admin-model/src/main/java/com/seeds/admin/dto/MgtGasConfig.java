package com.seeds.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MgtGasConfig {
    Long gasPrice;
    Long gasLimit;
    Long gasFee;
}
