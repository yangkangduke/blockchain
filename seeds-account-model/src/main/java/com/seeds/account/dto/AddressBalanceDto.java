package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBalanceDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 用户邮箱
     */
    String email;
    /**
     * 币种
     */
    String address;

    String tag;

    /**
     * 是否启用
     */
    Map<String, BigDecimal> balances;
}