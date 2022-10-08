package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 充提历史
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@TableName("chain_deposit_withdraw_his")
@ApiModel(value = "ChainDepositWithdrawHis对象", description = "充提历史")
@Data
@Builder
public class ChainDepositWithdrawHis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("version")
    private Long version;

    @ApiModelProperty("user id")
    private Long userId;

    @ApiModelProperty("chain")
    private Enum chain;

    @ApiModelProperty("from address")
    private String fromAddress;

    @ApiModelProperty("to address")
    private String toAddress;

    @ApiModelProperty("chain tx to address")
    private String txToAddress;

    @ApiModelProperty("currency")
    private String currency;

    private Integer internal;

    @ApiModelProperty("1 deposit, 2 withdraw")
    private Enum action;

    @ApiModelProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty("fee amount")
    private BigDecimal feeAmount;

    @ApiModelProperty("gasPrice")
    private Long gasPrice;

    @ApiModelProperty("gasLimit")
    private Long gasLimit;

    @ApiModelProperty("tx fee amount")
    private BigDecimal txFee;

    @ApiModelProperty("block number")
    private Long blockNumber;

    @ApiModelProperty("block hash")
    private String blockHash;

    @ApiModelProperty("tx hash")
    private String txHash;

    @ApiModelProperty("tx nonce")
    private String nonce;

    @ApiModelProperty("chain transaction value")
    private String txValue;

    @ApiModelProperty("tx 是否被replaced")
    private Boolean isReplace;

    @ApiModelProperty("是否运营介入")
    private Integer manual;

    @ApiModelProperty("是否是黑地址")
    private Integer blacklist;

    @ApiModelProperty("approve or reject comments")
    private String comments;

    private Integer status;

}
