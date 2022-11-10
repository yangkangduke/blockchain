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
public class DepositRuleSaveOrUpdateReq {

    private static final long serialVersionUID = 1L;
    private Long id;

    @ApiModelProperty("chain 1:eth  3 :tron")
    private Integer chain;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("最小")
    private BigDecimal minAmount;

    @ApiModelProperty("精度")
    private Integer decimals;

    @ApiModelProperty("自动上帐，免审核金额")
    private BigDecimal autoAmount;

    @ApiModelProperty("1: 开启 2: 禁用")
    private Integer status;

}
