package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: seeds-java
 * @description: 忘记密码验证
 * @author: yk
 * @create: 2022-08-04 14:39
 **/
@Data
public class ForgotPasswordVerifyReq {
    @ApiModelProperty(value = "验证码")
    @NotBlank
    private String encode;
    @ApiModelProperty(value = "账号")
    @NotBlank
    private String account;
}
