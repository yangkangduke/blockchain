package com.seeds.admin.dto.response;

import com.seeds.admin.dto.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *字典类别
 * @author yang.deng
 * @data 2022/8/1
 */


@Data
@ApiModel(value = "SysDictTypeResp", description = "系统字典类别系统")
public class SysDictTypeResp extends TreeNode<SysDictTypeResp> {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "字典编码")
    private Long dictCode;

    @ApiModelProperty(value = "dict_name")
    private String dictName;

    @ApiModelProperty(value = "父级编码")
    private String parentCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    private String sort;

}
