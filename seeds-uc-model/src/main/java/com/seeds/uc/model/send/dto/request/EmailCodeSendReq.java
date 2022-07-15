package com.seeds.uc.model.send.dto.request;

import com.seeds.uc.model.send.enums.AuthCodeUseTypeEnum;
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
@ApiModel(value = "邮箱验证码发送请求", description = "邮箱验证码发送请求")
public class EmailCodeSendReq {
    @NotBlank
    @Email
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    @ApiModelProperty(value = "用户类型", required = true)
    private AuthCodeUseTypeEnum useType;

}
