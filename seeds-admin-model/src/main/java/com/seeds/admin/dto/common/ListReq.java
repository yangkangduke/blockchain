package com.seeds.admin.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@ApiModel(value = "分页请求入参")
public class ListReq {

    @ApiModelProperty(value = "编号列表")
    @NotEmpty(message = "The id list cannot be empty")
    private Set<Long> ids;

}
