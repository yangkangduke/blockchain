package com.seeds.admin.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
@ApiModel(value = "SysOrgResp", description = "系统组织信息")
public class SysOrgResp {


    @ApiModelProperty("主键id")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("组织ID")
    private Long orgId;

    @ApiModelProperty("组织名称")
    private String orgName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("父级组织ID")
    private Long parentOrgId;

    @ApiModelProperty("父级组织名称")
    private String parentOrgName;

    @ApiModelProperty("负责人id")
    private String ownerId;

    @ApiModelProperty("负责人名字")
    private String ownerName;

    @ApiModelProperty("备注")

    private String comments;
    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("子组织")
    List<SysOrgResp> children = new ArrayList<>();
}