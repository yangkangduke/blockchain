package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * 组织管理
 *
 * @author hewei
 * @date 2022/7/22
 */

@Data
@ApiModel(value = "系统菜单信息")
public class SysOrgResp {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("组织ID")
    private Long orgId;

    @ApiModelProperty("组织名称")
    private String orgName;

    @ApiModelProperty("父级组织ID")
    private Long parentOrgId;

    @ApiModelProperty("父级组织名称")
    private String parentOrgName;

    @ApiModelProperty("负责人")
    private String owner;

    @ApiModelProperty("备注")

    private String comments;
    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("子组织")
    List<SysOrgResp> children = new ArrayList<>();
}