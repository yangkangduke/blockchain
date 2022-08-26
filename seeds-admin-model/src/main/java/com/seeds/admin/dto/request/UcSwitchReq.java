package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel(value = "UcSwitchReq", description = "uc停用/启用请求入参")
public class UcSwitchReq {

    @ApiModelProperty(value = "uc用户编号")
    private Long ucUserId;

    @ApiModelProperty(value = "停用/启用请求入参")
    @NotEmpty(message = "The request param cannot be empty")
    private List<SwitchReq> reqs;

}
