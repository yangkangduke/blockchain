package com.seeds.uc.dto.request;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

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
public class AccountActionHistoryReq extends PageReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("操作 1冲币 2提币")
    @NotNull
    private Integer action;

    @ApiModelProperty("币种")
    @NotNull
    private String currency;

    @ApiModelProperty("发送端地址")
    private String fromAddress;

    @ApiModelProperty("接收端地址")
    private String toAddress;

    @ApiModelProperty("状态")
    @NotNull
    private Integer status;

}
