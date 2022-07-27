package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "SysUserRoleReq", description = "用户分配角色")
public class SysUserRoleReq {

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "User id cannot be empty")
    private Long userId;

    @ApiModelProperty(value = "角色id列表")
    private List<Long> roleIds;

}
