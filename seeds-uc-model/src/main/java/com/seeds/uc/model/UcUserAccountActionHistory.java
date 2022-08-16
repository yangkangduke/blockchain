package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.seeds.uc.enums.AccountActionEnum;
import com.seeds.uc.enums.AccountTypeEnum;
import com.seeds.uc.enums.CurrencyEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户账号交易历史表
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@TableName("uc_user_account_action_history")
@ApiModel(value = "UcUserAccountActionHistory对象", description = "用户账号交易历史表")
@Data
@Builder
public class UcUserAccountActionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("修改时间")
    private Long updateTime;

    @ApiModelProperty("版本控制")
    @Version
    private Long version;

    @ApiModelProperty("操作: 1-冲币 2-提币")
    private AccountActionEnum action;

    @ApiModelProperty("账号类型 1-现货")
    private AccountTypeEnum accountType;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("币种")
    private CurrencyEnum currency;

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
    private Integer status;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("频道")
    private Integer channel;

    @ApiModelProperty("correlation")
    private String correlation;

    @ApiModelProperty("备注")
    private String comments;


}
