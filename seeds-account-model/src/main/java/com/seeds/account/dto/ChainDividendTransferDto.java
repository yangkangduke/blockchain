package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainDividendTransferDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private int chain;

    /**
     * 奖励发放的币种
     */
    private String currency;
    /**
     * 奖励发放的地址
     */
    private String address;

    /**
     * 1-minter, 2-kine_ranch, 3-farming
     */
    private int type;

    @Positive(message = "amount should be greater than 0")
    private BigDecimal amount;

    @JsonIgnore
    private Long operationId;
}
