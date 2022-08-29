package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 系统游戏类别信息
 * 
 * @author hang.yu
 * @date 2022/8/25
 */
@Data
@ApiModel(value = "SysGameTypeModifyReq", description = "系统游戏类别信息")
public class SysGameTypeModifyReq {

	/**
	 * NFT类别id
	 */
	@ApiModelProperty("NFT类别id")
	@NotNull(message = "Game type id cannot be empty")
	private Long id;

	/**
	 * 父类别code，一级类别为空
	 */
	@ApiModelProperty("父类别code，一级类别为空")
	private String parentCode;

	/**
	 * 类别编码
	 */
	@ApiModelProperty("类别编码")
	@NotBlank(message = "Game type code cannot be empty")
	private String code;

	/**
	 * 类别名称
	 */
	@ApiModelProperty("类别名称")
	@NotBlank(message = "Game type name cannot be empty")
	private String name;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer sort;

}