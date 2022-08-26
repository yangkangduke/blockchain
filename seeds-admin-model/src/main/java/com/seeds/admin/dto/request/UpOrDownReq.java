package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(value = "UpOrDownReq", description = "上架/下架请求入参")
public class UpOrDownReq {

    @ApiModelProperty(value = "编号")
    @NotNull(message = "The id cannot be empty")
    private Long id;

    @ApiModelProperty(value = "状态")
    @NotNull(message = "The status cannot be empty")
    private Integer status;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "单位")
    private String unit;

}
