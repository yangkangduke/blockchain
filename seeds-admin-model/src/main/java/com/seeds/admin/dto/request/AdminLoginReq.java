package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "登录表单")
public class AdminLoginReq {

    @ApiModelProperty(value = "账户")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "Captcha cannot be empty")
    private String opt;

    @ApiModelProperty(value = "登录方式，1 phone， 2 account")
    @NotBlank(message = "Login method cannot be empty")
    private String authType;

}
