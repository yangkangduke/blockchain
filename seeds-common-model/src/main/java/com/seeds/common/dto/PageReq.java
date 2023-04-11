package com.seeds.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "分页请求入参")
public class PageReq implements Serializable {

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "数据条数")
    private Integer size = 10;

}
