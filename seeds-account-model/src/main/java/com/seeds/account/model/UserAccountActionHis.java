package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * t_user_account_action_his
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Data
@Builder
@TableName("ac_user_account_action_his")
@ApiModel(value = "UserAccountActionHis对象", description = "t_user_account_action_his")
public class UserAccountActionHis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("version")
    private Integer version;

    private AccountAction action;

    private String source;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("fromUserId")
    private Long fromUserId;

    @ApiModelProperty("toUserId")
    private Long toUserId;

    private String currency;

    @ApiModelProperty("amount")
    private BigDecimal amount;

    private CommonActionStatus status;

}
