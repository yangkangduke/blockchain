package com.seeds.account.dto.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 充币规则新增/编辑
 * </p>
 *
 * @author hewei
 * @since 2022-11-7
 */
@Data
@ApiModel(value = "DepositRuleSaveOrUpdateReq", description = "充币规则新增/编辑")
public class DepositRuleSaveOrUpdateReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("chain")
    private Integer chain;

    @ApiModelProperty("currency")
    private String currency;

    @ApiModelProperty("min amount")
    private BigDecimal minAmount;

    private Integer decimals;

    @ApiModelProperty("auto deposit amount")
    private BigDecimal autoAmount;

    @ApiModelProperty("1: enable 2: disabled")
    private Integer status;

}
