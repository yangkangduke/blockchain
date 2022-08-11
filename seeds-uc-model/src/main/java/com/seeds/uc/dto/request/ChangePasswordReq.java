package com.seeds.uc.dto.request;

import com.seeds.uc.enums.ClientAuthTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
public class ChangePasswordReq {
    @ApiModelProperty(value = "原密码", required = true)
    @NotBlank
    private String oldPassword;
    @NotBlank
    @ApiModelProperty(value = "新密码", required = true)
    private String password;
    @ApiModelProperty(value = "授权类型：2-email, 3-ga", required = true)
    private ClientAuthTypeEnum authType;
    @NotBlank
    @ApiModelProperty(value = "验证码", required = true)
    private String code;
}
