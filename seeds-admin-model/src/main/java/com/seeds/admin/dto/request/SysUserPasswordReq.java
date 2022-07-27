package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "SysUserPasswordReq", description = "后台用户密码")
public class SysUserPasswordReq {

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "User id cannot be empty")
    private Long userId;

    @ApiModelProperty(value = "原密码")
    @NotBlank(message = "Old Password cannot be empty")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "New password cannot be empty")
    private String newPassword;

}
