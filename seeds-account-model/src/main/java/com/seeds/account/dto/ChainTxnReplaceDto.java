package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.ChainTxnReplaceAppType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChainTxnReplaceDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @Min(value = ChainTxnReplaceAppType.MIN_CODE, message = "type is not valid")
    @Max(value = ChainTxnReplaceAppType.MAX_CODE, message = "type is not valid")
    private Integer type;

    @NotNull(message = "txHash can not be null")
    private String txHash;

    @Positive(message = "gasLimit should be greater than 0")
    private BigInteger gasLimit;

    @Positive(message = "gasPrice should be greater than 0")
    private BigInteger gasPrice;

    private Integer chain;

    @JsonIgnore
    private Long operationId;
}
