package com.seeds.uc.dto.request;

import com.seeds.uc.enums.ClientAuthTypeEnum;
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
public class LoginReq {

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank
    @Email
    private String email;
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String password;
    @ApiModelProperty(value = "2fa方式: 2email 3ga")
    private ClientAuthTypeEnum authType;


}