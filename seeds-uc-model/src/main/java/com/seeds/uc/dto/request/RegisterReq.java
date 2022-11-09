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
public class RegisterReq {

    @ApiModelProperty(value = "email", required = true)
    @Email
    private String email;
    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank
    private String code;
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String password;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

}
