package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.ChainTxnReplaceAppType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChainTxnReplayDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @Min(value = ChainTxnReplaceAppType.MIN_CODE, message = "type is not valid")
    @Max(value = ChainTxnReplaceAppType.MAX_CODE, message = "type is not valid")
    private Integer type;

    @NotNull(message = "id can not be null")
    private Long id;

    @NotNull(message = "txHash can not be null")
    private String txHash;
}
