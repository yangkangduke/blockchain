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
 *
 * @author yk
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
    @ApiModelProperty(value = "chain的传值，1：eth链erc20  3：tron链trc20")
    int chain;
    @ApiModelProperty(value = "币种")
    String currency;
    @ApiModelProperty(value = "数量")
    BigDecimal amount;
    @ApiModelProperty(value = "手续费")
    BigDecimal fee;
    @ApiModelProperty(value = "地址")
    String address;

    /**
     * 用户使用邮件，Google Auth验证时会得到一个提币Token
     */
    @ApiModelProperty(value = "用户使用邮件，Google Auth验证时会得到一个提币Token")
    String withdrawToken;

}
