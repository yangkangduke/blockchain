package com.seeds.admin.dto;
import com.seeds.account.enums.ChainAction;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MgtDepositWithdrawDto {

    @ApiModelProperty(value = "id")
    long id;

    @ApiModelProperty(value = "创建时间")
    long createTime;

    @ApiModelProperty(value = "更新时间")
    long updateTime;

    @ApiModelProperty(value = "用户唯一标识")
    long userId;

    @ApiModelProperty(value = "内部的")
    int internal;

    @ApiModelProperty(value = "发起钱包账户地址")
    String fromAddress;

    @ApiModelProperty(value = "到达的钱包账户地址")
    String toAddress;

    @ApiModelProperty(value = "USDT")
    String currency;

    @ApiModelProperty(value = "1：充币 2：提币,可用值:DEPOSIT,WITHDRAW")
    ChainAction action;

    @ApiModelProperty(value = "数量")
    String amount;

    @ApiModelProperty(value = "手续费")
    String feeAmount;

    @ApiModelProperty(value = "充币的看DepositStatus枚举，提币的看WithdrawStatus枚举")
    int status;

    @ApiModelProperty(value = "是否是黑名单地址 0不是 1是")
    int blacklist;
    /**
     * 审核comments
     */
    @ApiModelProperty(value = "评语")
    String comments;

    @ApiModelProperty(value = "chain")
    Integer chain;

    /**
     * 交易Hash
     */
    @ApiModelProperty(value = "交易Hash")
    String txHash;
}
