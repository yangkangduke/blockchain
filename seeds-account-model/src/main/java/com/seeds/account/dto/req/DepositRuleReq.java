package com.seeds.account.dto.req;

import com.seeds.common.enums.Chain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 充币规则请求req
 * </p>
 *
 * @author hewei
 * @since 2022-11-7
 */
@Data
@ApiModel(value = "DepositRulePageReq", description = "充币规则请求req")
public class DepositRuleReq {

    private static final long serialVersionUID = 1L;

    /**
     * @see com.seeds.account.enums.CommonStatus
     */
    @ApiModelProperty(value = "状态 1：启用 2：停用")
    private Integer status;
    /**
     * @see Chain
     */
    @ApiModelProperty(value = "链 1：eth 3: tron")
    private Integer chain;

}