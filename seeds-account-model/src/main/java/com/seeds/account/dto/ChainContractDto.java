package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 链上合约配置
 *
 * @author milo
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainContractDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * @see com.kine.common.enums.Chain
     */
    private int chain;
    private String currency;
    private String address;
    private int decimals;
    private String transferSign;
    private Boolean isStakingAsset;

    private int status;
}
