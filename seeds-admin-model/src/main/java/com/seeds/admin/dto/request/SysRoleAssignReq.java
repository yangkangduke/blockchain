package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author hang.yu
 * @date 2022/8/03
 */
@Data
@ApiModel(value = "SysUserRoleReq", description = "角色分配用户")
public class SysRoleAssignReq {

    @ApiModelProperty(value = "用户id列表")
    @NotEmpty(message = "User id list cannot be empty")
    private List<Long> userIds;

    @ApiModelProperty(value = "角色id")
    @NotNull(message = "Role id cannot be empty")
    private Long roleId;

}
