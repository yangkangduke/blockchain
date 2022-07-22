package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "停用/启用请求入参")
public class SwitchReq {

    @ApiModelProperty(value = "编号")
    @NotNull(message = "The id cannot be empty")
    private Long id;

    @ApiModelProperty(value = "状态")
    @NotNull(message = "The status cannot be empty")
    private Integer status;

}
