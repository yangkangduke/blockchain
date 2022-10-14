package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 链上合约配置
 *
 * @author yk
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainLpStakingDto implements Serializable {
    private static final long serialVersionUID = -1L;

    private Chain chain;
    private String token0;
    private String token1;
    private String token;
    private String token0Address;
    private String token1Address;
    private String tokenAddress;
    private String farmingAddress;
    private int token0Decimal;
    private int token1Decimal;
    private int tokenDecimal;

    private int status;

    private int type;
}
