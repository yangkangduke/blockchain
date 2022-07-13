package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分页请求入参")
public class PageQuery {

    @ApiModelProperty(value = "当前页码")
    private Integer current;

    @ApiModelProperty(value = "数据条数")
    private Integer size;

}
