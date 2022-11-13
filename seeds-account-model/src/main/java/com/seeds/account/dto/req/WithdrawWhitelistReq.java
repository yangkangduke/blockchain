package com.seeds.account.dto.req;

import com.seeds.common.enums.Chain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 提币白名单请求req
 * </p>
 *
 * @author dengyang
 * @since 2022-11-12
 */
@Data
@ApiModel(value = "WithdrawWhitelistPageReq", description = "提币白名单请求req")
public class WithdrawWhitelistReq {

    /**
     * @see  com.seeds.account.enums.CommonStatus
     */
    @ApiModelProperty(value = "状态 1：启用 2：停用")
    private Integer status;
    /**
     * @see Chain
     */
    @ApiModelProperty(value = "链 1：eth 3: tron")
    private Integer chain;
}
