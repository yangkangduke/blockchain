package com.seeds.admin.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 字典信息
 * @author yang.deng
 * @data 2022/8/1
 */
@Data
@ApiModel(value = "SysDictTypeAddReq", description = "字典信息")
public class SysDictTypeAddReq {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    @NotNull(message = "DictType id cannot be empty")
    private String id;

    /**
     *字典编码
     */
    @ApiModelProperty("字典编码")
    @NotBlank(message = "DictType code cannot be empty")
    private String dictCode;

    /**
     * 字典名称
     */
    @ApiModelProperty("字典名称")
    @NotBlank(message = "DictType name cannot be empty")
    private String dictName;

    /**
     *父级编码
     */
    @ApiModelProperty("父级编码")
    private String parentCode;

    /**
     *备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private String sort;
}
