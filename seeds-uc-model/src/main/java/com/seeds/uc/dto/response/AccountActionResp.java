package com.seeds.uc.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户账号交易
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountActionResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("操作 1冲币 2提币")
    @NotNull
    private Integer action;

    @ApiModelProperty("账号类型 1 现货")
    @NotNull
    private Integer accountType;

    @ApiModelProperty("币种")
    @NotNull
    private String currency;

    @ApiModelProperty("链")
    private String chain;

    @ApiModelProperty("发送端地址")
    private String fromAddress;

    @ApiModelProperty("接收端地址")
    private String toAddress;

    @ApiModelProperty("哈希值")
    private String txHash;

    @ApiModelProperty("区块号")
    private Long blockNumber;

    @ApiModelProperty("区块的hash值")
    private String blockHash;

    @ApiModelProperty("状态")
    @NotNull
    private Integer status;

    @ApiModelProperty("金额")
    private String amount;

    @ApiModelProperty("手续费")
    private String fee;

    @ApiModelProperty("频道")
    private Integer channel;

    @ApiModelProperty("correlation")
    private String correlation;

    @ApiModelProperty("备注")
    private String comments;


}
