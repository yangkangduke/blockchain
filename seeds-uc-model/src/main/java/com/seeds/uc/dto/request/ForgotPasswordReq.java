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
public class ForgotPasswordReq {
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank
    @Email
    private String account;
    private ClientAuthTypeEnum authType;
}
