package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "注册请求", description = "注册请求")
public class RegisterReq {

    @ApiModelProperty(value = "登陆账号", required = true)
    @NotBlank
    @Email
    private String account;
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String password;


}
