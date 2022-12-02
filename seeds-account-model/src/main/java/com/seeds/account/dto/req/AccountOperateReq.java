package com.seeds.account.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 操作账户
 * </p>
 *
 * @author hang.yu
 * @since 2022-11-02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperateReq implements Serializable {

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty("用户id")
    private Long userId;

}
