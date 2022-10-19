package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.account.enums.ChainAction;
import com.seeds.common.enums.Chain;
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
public class ChainDepositWithdrawHisDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "id")
    long id;
    @ApiModelProperty(value = "创建时间")
    long createTime;
    @ApiModelProperty(value = "修改时间")
    long updateTime;
    @ApiModelProperty(value = "用户id")
    long userId;
    @ApiModelProperty(value = "是否内部 0：不是 1：是")
    int internal;
    @ApiModelProperty(value = "fromAddress")
    String fromAddress;
    @ApiModelProperty(value = "toAddress")
    String toAddress;
    @ApiModelProperty(value = "币种")
    String currency;
    @ApiModelProperty(value = "1：充币 2：提币")
    ChainAction action;
    @ApiModelProperty(value = "数量")
    BigDecimal amount;
    @ApiModelProperty(value = "手续费")
    BigDecimal feeAmount;
    /**
     * 审核comments
     */
    @ApiModelProperty(value = "审核comments")
    String comments;
    /**
     * 是否是黑名单地址 0不是 1是
     */
    @ApiModelProperty(value = "是否是黑名单地址 0不是 1是")
    int blacklist;
    @ApiModelProperty(value = "")
    int status;
    @ApiModelProperty(value = "chain")
    Chain chain;
    /**
     * 交易Hash
     */
    @ApiModelProperty(value = "交易Hash")
    String txHash;
}
