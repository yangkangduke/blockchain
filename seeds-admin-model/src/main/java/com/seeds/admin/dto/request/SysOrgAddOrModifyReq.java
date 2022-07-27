package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hewei
 * @date: 2022/7/22
 */

@Data
@ApiModel(value = "SysOrgAddOrModifyReq", description = "组织信息")
public class SysOrgAddOrModifyReq {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("父级组织ID")
    private Long parentOrgId;

    @ApiModelProperty("组织名称")
    @NotBlank(message = "orgName cannot be empty")
    private String orgName;

    @ApiModelProperty("负责人")
    private String owner;

    @ApiModelProperty("备注")
    private String comments;

    @ApiModelProperty("排序字段")
    private Integer sort;

}
