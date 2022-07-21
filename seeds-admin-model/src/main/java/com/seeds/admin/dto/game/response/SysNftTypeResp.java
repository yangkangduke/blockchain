package com.seeds.admin.dto.game.response;

import com.seeds.admin.dto.common.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统NFT类别
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "系统NFT类别信息")
public class SysNftTypeResp extends TreeNode<SysNftTypeResp> {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "编码")
	private String code;

	@ApiModelProperty(value = "上级编码")
	private String parentCode;

	@ApiModelProperty(value = "类别名称")
	private String name;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

	@ApiModelProperty(value = "上级菜单名称")
	private String parentName;


}