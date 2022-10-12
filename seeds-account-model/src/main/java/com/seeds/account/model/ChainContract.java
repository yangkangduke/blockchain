package com.seeds.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 链上合约配置
 *
 * @author milo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainContract {

    private long id;
    private long createTime;
    private long updateTime;
    private long version;

    /**
     * @see com.seeds.common.enums.Chain
     */
    private int chain;
    private String currency;
    private String address;
    private int decimals;
    private String transferSign;
    private Boolean isStakingAsset;

    private int status;
}
