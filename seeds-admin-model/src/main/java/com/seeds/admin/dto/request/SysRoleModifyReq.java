package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统角色
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@ApiModel(value = "SysRoleModifyReq", description = "系统角色信息")
public class SysRoleModifyReq {

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	@NotNull(message = "User id cannot be empty")
	private Long id;

	/**
	 * 角色编码
	 */
	@ApiModelProperty("角色编码")
	@NotBlank(message = "Role code cannot be empty")
	private String roleCode;

	/**
	 * 角色名称
	 */
	@ApiModelProperty("角色名称")
	@NotBlank(message = "Role name cannot be empty")
	private String roleName;

	/**
	 * 菜单ID列表
	 */
	@ApiModelProperty(value = "菜单ID列表")
	private List<Long> menuIdList;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;

}