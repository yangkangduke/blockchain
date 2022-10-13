package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.seeds.account.enums.CommonStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 充提规则
 * </p>
 *
 * @author yk
 * @since 2022-10-10
 */
@TableName("deposit_rule")
@ApiModel(value = "DepositRule对象", description = "充提规则")
@Data
@Builder
public class DepositRule implements Serializable {

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
    private int chain;

    @ApiModelProperty("currency")
    private String currency;

    @ApiModelProperty("min amount")
    private BigDecimal minAmount;

    private Integer decimals;

    @ApiModelProperty("auto deposit amount")
    private BigDecimal autoAmount;

    private CommonStatus status;

}
