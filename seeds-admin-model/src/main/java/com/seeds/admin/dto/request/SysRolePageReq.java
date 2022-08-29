package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@ApiModel(value = "SysRolePageReq", description = "用户角色请求入参")
public class SysRolePageReq extends PageReq {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

}
