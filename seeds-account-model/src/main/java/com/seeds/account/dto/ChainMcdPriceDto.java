package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author guocheng
 * @date 2021/1/6
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainMcdPriceDto implements Serializable {

    private static final long serialVersionUID = -1L;

    private Chain chain;

    /**
     * symbol = MCD
     */
    private String symbol;

    /**
     * 计算最新的mcd price
     */
    private BigDecimal mcdPrice;

    /**
     * 初始配置的mcd price
     */
    private BigDecimal initMcdPrice;

    /**
     * reporter address
     */
    private String address;

    /**
     *
     */
    private BigDecimal deltaKusd;

    private BigDecimal mcdTotal;

    /**
     * 当前链上mcd value (mcd price * mcd total)
     */
    private BigDecimal chainMcdValue;

    /**
     * 新的mcd value (new mcd price * mcd total)
     */
    private BigDecimal newMcdValue;

    private BigDecimal kusdTotal;

}
