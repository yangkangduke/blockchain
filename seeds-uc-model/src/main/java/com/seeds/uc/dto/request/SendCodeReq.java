package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SendCodeReq {
    @ApiModelProperty(value = "地址", required = true)
    @NotBlank
    private String address;
    @ApiModelProperty(value = "用户类型", required = true)
    @NotNull
    private AuthCodeUseTypeEnum useType;

}