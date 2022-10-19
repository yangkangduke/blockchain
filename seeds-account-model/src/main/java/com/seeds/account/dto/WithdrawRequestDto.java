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
     * 用户使用短信，邮件，Google Auth验证时会得到一个提币Token
     */
    @ApiModelProperty(value = "用户使用邮件、Google Auth验证时会传。目前暂时关闭了，可以不用传")
    String withdrawToken;

    /**
     * 用户使用Metamask验证时会得到下面3个信息
     */
    @ApiModelProperty(value = "用户使用Metamask验证时会传，publicAddress。目前暂时关闭了，可以不用传")
    String publicAddress;
    @ApiModelProperty(value = "用户使用Metamask验证时会传，signature。目前暂时关闭了，可以不用传")
    String signature;
    @ApiModelProperty(value = "用户使用Metamask验证时会传，msg。目前暂时关闭了，可以不用传")
    String msg;
}
