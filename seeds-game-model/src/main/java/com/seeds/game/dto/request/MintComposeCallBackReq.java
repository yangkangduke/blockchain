package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: hewei
 * @date 2023/4/3
 */
@Data
public class MintComposeCallBackReq {

    @ApiModelProperty(value = "是否自动托管 1 是 0 否")
    private Integer isDeposit;

    @ApiModelProperty(value = "销毁装备地址")
    private String mintAddresses;

    @ApiModelProperty(value = "销毁装备的签名")
    private String sig;

    @ApiModelProperty(value = "用户钱包地址")
    private String walletAddress;

    @ApiModelProperty("事件id")
    @NotNull
    private Long orderId;
}
