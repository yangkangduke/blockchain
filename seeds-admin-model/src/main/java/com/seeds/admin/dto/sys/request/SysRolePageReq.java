package com.seeds.admin.dto.sys.request;

import com.seeds.admin.dto.common.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@ApiModel(value = "用户角色请求入参")
public class SysRolePageReq extends PageReq {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

}
