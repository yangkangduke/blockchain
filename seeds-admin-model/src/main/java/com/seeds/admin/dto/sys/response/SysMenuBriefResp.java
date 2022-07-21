package com.seeds.admin.dto.sys.response;

import com.seeds.admin.dto.common.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统菜单简略信息
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "系统菜单简略信息")
public class SysMenuBriefResp extends TreeNode<SysMenuBriefResp> {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "编码")
	private String code;

	@ApiModelProperty(value = "上级编码")
	private String parentCode;

	@ApiModelProperty(value = "菜单名称")
	private String name;

	@ApiModelProperty(value = "上级菜单名称")
	private String parentName;

}