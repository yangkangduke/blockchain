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
public class MintEquipCallBackReq {

    @ApiModelProperty(value = "转账成功后的签名信息：校验充值是否正确")
    @NotBlank
    private String feeHash;

    @ApiModelProperty(value = "用户钱包地址：当不需要托管的时候，新mint出来的token需要转移给他")
    private String toUserAddress;

    @ApiModelProperty("事件id")
    @NotNull
    private Long orderId;
}
