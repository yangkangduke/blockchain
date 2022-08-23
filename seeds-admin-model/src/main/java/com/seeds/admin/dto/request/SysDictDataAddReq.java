package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 字典数据信息
 * @author yang.deng
 * @date  2022/8/15
 */
@Data
@ApiModel(value = "SysDictDataAddReq", description = "字典数据信息")
public class SysDictDataAddReq {

    /**
     * 字典类别id
     */
    @ApiModelProperty("字典类别id")
    @NotNull(message = "Dict type id cannot be empty")
    private Long dictTypeId;

    /**
     *名称
     */
    @ApiModelProperty("名称")
    private String dictLabel;

    /**
     *属性值
     */
    @ApiModelProperty("属性值")
    private String dictValue;

    /**
     *标记
     */
    @ApiModelProperty("标记")
    private String remark;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private String sort;
}
