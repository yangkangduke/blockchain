package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.seeds.account.enums.CommonStatus;
import com.seeds.common.enums.Chain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 提币规则
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@TableName("withdraw_rule")
@ApiModel(value = "WithdrawRule对象", description = "提币规则")
@Data
@Builder
public class WithdrawRule implements Serializable {

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

    @ApiModelProperty("chain")
    private Chain chain;

    @ApiModelProperty("currency")
    private String currency;

    @ApiModelProperty("min amount")
    private BigDecimal minAmount;

    @ApiModelProperty("max amount")
    private BigDecimal maxAmount;

    @ApiModelProperty("intrady amount")
    private BigDecimal intradayAmount;

    @ApiModelProperty("fee type")
    private Integer feeType;

    @ApiModelProperty("fee amount")
    private BigDecimal feeAmount;

    private Integer decimals;

    @ApiModelProperty("auto withdraw amount")
    private BigDecimal autoAmount;

    @ApiModelProperty("是否内部提币面手续费")
    private String zeroFeeOnInternal;

    private CommonStatus status;


}