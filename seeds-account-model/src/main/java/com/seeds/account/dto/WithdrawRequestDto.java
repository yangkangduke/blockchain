package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequestDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     *
     * @see com.seeds.common.enums.Chain
     */
    int chain;

    String currency;
    BigDecimal amount;
    BigDecimal fee;
    String address;

    /**
     * 用户使用短信，邮件，Google Auth验证时会得到一个提币Token
     */
    String withdrawToken;

    /**
     * 用户使用Metamask验证时会得到下面3个信息
     */
    String publicAddress;
    String signature;
    String msg;
}
