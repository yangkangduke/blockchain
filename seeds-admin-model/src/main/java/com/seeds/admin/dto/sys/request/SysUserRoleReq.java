package com.seeds.admin.dto.sys.request;

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
@ApiModel(value = "用户分配角色")
public class SysUserRoleReq {

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "User id cannot be empty")
    private Long userId;

    @ApiModelProperty(value = "角色id")
    @NotBlank(message = "Role id Password cannot be empty")
    private Long roleId;

}
