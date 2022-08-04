package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @program: seeds-java
 * @description: 修改邮箱
 * @author: yk
 * @create: 2022-08-04 13:35
 **/
@Data
@Builder
public class UpdateEmailReq {

    @ApiModelProperty("邮箱")
    @Email
    private String email;
    @ApiModelProperty("验证码")
    @NotBlank
    private String code;
}
