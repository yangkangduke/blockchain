package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hewei
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainDepositWithdrawMonitorDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "1 eth 3 tron")
    private int chain;

    @ApiModelProperty(value = "1 deposit, 2 withdraw")
    private int action;

    @ApiModelProperty(value = "金额")
    BigDecimal amount;

}
