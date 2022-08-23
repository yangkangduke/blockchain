package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *字典数据
 * @author yang.deng
 * @data 2022/8/15
 */
@Data
@ApiModel(value = "SysDictdataResp", description = "系统字典数据系统")
public class SysDictDataResp {

    @ApiModelProperty(value = "字典id")
    private Long id;

    @ApiModelProperty(value = "字典类别id")
    private Long dictTypeId;

    @ApiModelProperty(value = "字典名称")
    private String dictLabel;

    @ApiModelProperty(value = "属性值")
    private Long dictValue;

    @ApiModelProperty(value = "标记")
    private Long remark;

    @ApiModelProperty(value = "排序")
    private Long sort;
}
