package com.seeds.admin.dto.response;

import com.seeds.admin.dto.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统菜单
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@ApiModel(value = "SysMenuResp", description = "系统菜单信息")
public class SysMenuResp extends TreeNode<SysMenuResp> {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "编码")
	private String code;

	@ApiModelProperty(value = "上级编码")
	private String parentCode;

	@ApiModelProperty(value = "菜单名称")
	private String name;

	@ApiModelProperty(value = "菜单URL")
	private String url;

	@ApiModelProperty(value = "类型  0：菜单   1：按钮")
	private Integer type;

	@ApiModelProperty(value = "菜单图标")
	private String icon;

	@ApiModelProperty(value = "授权(多个用逗号分隔，如：sys:user:list,sys:user:save)")
	private String permissions;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

	@ApiModelProperty(value = "上级菜单名称")
	private String parentName;

}