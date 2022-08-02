package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TwoFactorLoginReq {
    @NotBlank
    @ApiModelProperty(value = "token", required = true)
    private String token;
    @NotBlank
    @ApiModelProperty(value = "授权的code", required = true)
    private String authCode;
}
