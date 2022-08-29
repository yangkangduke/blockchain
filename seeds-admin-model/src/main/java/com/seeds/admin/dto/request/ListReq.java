package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@ApiModel(value = "ListReq", description = "列表请求入参")
public class ListReq {

    @ApiModelProperty(value = "编号列表")
    @NotEmpty(message = "The id list cannot be empty")
    private Set<Long> ids;

}
