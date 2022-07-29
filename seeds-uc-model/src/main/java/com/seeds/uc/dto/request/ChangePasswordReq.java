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
public class ChangePasswordReq {
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank
    private String account;
    @NotBlank
    private String password;
    @ApiModelProperty(value = "2emai 3ga")
    private ClientAuthTypeEnum authTypeEnum;
    @NotBlank
    private String code;
}
