package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 系统角色
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@ApiModel(value = "SysRoleResp", description = "系统角色信息")
public class SysRoleResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "角色名称")
	private String roleName;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

	@ApiModelProperty(value = "菜单ID列表")
	private Set<Long> menuIdList;

}