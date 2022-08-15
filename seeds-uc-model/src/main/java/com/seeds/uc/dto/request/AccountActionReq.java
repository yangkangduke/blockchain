package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户账号交易
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Data
@Builder
public class AccountActionReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("操作 1冲币 2提币")
    @NotNull
    private Integer action;

    @ApiModelProperty("发送端地址")
    private String fromAddress;

    @ApiModelProperty("接收端地址")
    private String toAddress;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("备注")
    private String comments;


}
