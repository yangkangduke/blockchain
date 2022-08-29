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
@ApiModel(value = "ForgetPasswordReq", description = "忘记密码表单")
public class ForgetPasswordReq {

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "Mobile cannot be empty")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "Captcha cannot be empty")
    private String opt;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "Password cannot be empty")
    private String newPassword;

}
