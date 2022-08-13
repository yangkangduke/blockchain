package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hewei
 * @date 2022/8/8
 */
@Data
@ApiModel(value = "SysLogPageReq", description = "系统日志分页请求入参")
public class SysLogPageReq extends PageReq {

    @ApiModelProperty("操作人名字")
    private String operator;

    @ApiModelProperty("具体操作")
    private String operation;

}
