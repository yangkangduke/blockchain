package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
public class ResetGaReq {
    @ApiModelProperty(value = "账号", required = true)
    @Email
    private String email;
    @NotBlank
    @ApiModelProperty(value = "邮箱验证码", required = true)
    private String code;
}
