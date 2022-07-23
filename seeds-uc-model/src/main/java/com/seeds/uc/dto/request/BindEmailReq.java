package com.seeds.uc.dto.request;


import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
public class BindEmailReq {
    @ApiModelProperty(value = "邮箱", required = true)
    @Email
    @NotBlank
    private String email;
    @ApiModelProperty(value = "用户类型", required = true)
    @NotNull
    private AuthCodeUseTypeEnum useType;
    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank
    private String code;

}
