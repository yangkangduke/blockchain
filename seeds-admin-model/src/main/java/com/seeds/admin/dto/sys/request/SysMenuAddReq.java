package com.seeds.admin.dto.sys.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 系统菜单信息
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@ApiModel(value = "系统菜单信息")
public class SysMenuAddReq {

	/**
	 * 父菜单code，一级菜单为空
	 */
	@ApiModelProperty("父菜单code，一级菜单为空")
	private String parentCode;

	/**
	 * 菜单编码
	 */
	@ApiModelProperty("菜单编码")
	@NotBlank(message = "Menu code cannot be empty")
	private String code;

	/**
	 * 菜单名称
	 */
	@ApiModelProperty("菜单名称")
	@NotBlank(message = "Menu name cannot be empty")
	private String name;

	/**
	 * 菜单URL
	 */
	@ApiModelProperty("菜单URL")
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：sys:user:list,sys:user:save)
	 */
	@ApiModelProperty("授权(多个用逗号分隔，如：sys:user:list,sys:user:save)")
	private String permissions;

	/**
	 * 类型   0：菜单   1：按钮
	 */
	@ApiModelProperty("类型   0：菜单   1：按钮")
	@NotNull(message = "Menu type cannot be empty")
	private Integer type;

	/**
	 * 菜单图标
	 */
	@ApiModelProperty("菜单图标")
	private String icon;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer sort;

	/**
	 * 上级菜单名称
	 */
	@ApiModelProperty("上级菜单名称")
	private String parentName;

}