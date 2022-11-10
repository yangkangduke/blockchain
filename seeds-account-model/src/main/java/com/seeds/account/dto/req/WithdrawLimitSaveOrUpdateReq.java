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
public class WithdrawLimitSaveOrUpdateReq {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "最小")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "最大")
    private BigDecimal maxAmount;

    @ApiModelProperty(value = "当日限额")
    private BigDecimal intradayAmount;

    @ApiModelProperty(value = "免审核提币")
    private BigDecimal autoAmount;

    @ApiModelProperty(value = "是否内部提币面手续费  false、true")
    private String zeroFeeOnInternal;

}
