package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainGasPriceConfigDto implements Serializable {
    private static final long serialVersionUID = -1L;

    int chain;

    long defaultGasPrice;

    BigDecimal gasPriceVolatility;
}
