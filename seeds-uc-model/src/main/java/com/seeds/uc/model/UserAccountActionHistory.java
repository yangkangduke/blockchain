package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * t_user_account_action_history
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@TableName("user_account_action_history")
@ApiModel(value = "UserAccountActionHistory对象", description = "t_user_account_action_history")
@Data
@Builder
public class UserAccountActionHistory implements Serializable {

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

    private Integer action;

    private Integer accountType;

    @ApiModelProperty("userId")
    private Long userId;

    private String currency;

    private String chain;

    private String fromAddress;

    private String toAddress;

    private String txHash;

    private Long blockNumber;

    private String blockHash;

    private Integer status;

    private BigDecimal amount;

    private BigDecimal fee;

    private Integer channel;

    private String correlation;

    private String comments;


}
