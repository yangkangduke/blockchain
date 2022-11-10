package com.seeds.account.dto.req;

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
public class WithdrawRuleSaveOrUpdateReq {

    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * @see com.seeds.common.enums.Chain
     */
    @ApiModelProperty(value = "1：ETH 3：TRON")
    private Integer chain;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty("最小")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "手续费类型")
    private Integer feeType;

    @ApiModelProperty(value = "手续费")
    BigDecimal feeAmount;

    @ApiModelProperty(value = "金额精度")
    private Integer decimals;

    @ApiModelProperty(value = " 1启用 2停用")
    private Integer status;

    @ApiModelProperty("max amount")
    private BigDecimal maxAmount;

    @ApiModelProperty("intrady amount")
    private BigDecimal intradayAmount;

    @ApiModelProperty("auto withdraw amount")
    private BigDecimal autoAmount;

    @ApiModelProperty("是否内部提币面手续费  false、true")
    private String zeroFeeOnInternal;

}
