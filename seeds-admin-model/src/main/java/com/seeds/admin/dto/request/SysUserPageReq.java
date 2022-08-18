package com.seeds.admin.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "SysUserPageReq", description = "用户列表请求入参")
public class SysUserPageReq extends PageReq {

    @ApiModelProperty(value = "姓名/手机号")
    private String nameOrMobile;

    @ApiModelProperty(value = "部门id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

}
