package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.account.enums.CommonStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 提币白名单
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@TableName("ac_withdraw_whitelist")
@ApiModel(value = "WithdrawWhitelist对象", description = "提币白名单")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawWhitelist implements Serializable {

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

    @ApiModelProperty(value = "1：ETH 3：TRON")
    private Integer chain;

    @ApiModelProperty("currency")
    private String currency;

    @ApiModelProperty("max amount")
    private BigDecimal maxAmount;

    @ApiModelProperty("intrady amount")
    private BigDecimal intradayAmount;

    @ApiModelProperty("auto withdraw amount")
    private BigDecimal autoAmount;

    @ApiModelProperty("comments")
    private String comments;

    @ApiModelProperty("状态，1:启用 2：停用")
    private Integer status;

}
