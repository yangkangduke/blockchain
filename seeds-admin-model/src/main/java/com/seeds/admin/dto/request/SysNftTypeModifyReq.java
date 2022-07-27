package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 系统NFT类别信息
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "SysNftTypeModifyReq", description = "系统NFT类别信息")
public class SysNftTypeModifyReq {

	/**
	 * NFT类别id
	 */
	@ApiModelProperty("NFT类别id")
	@NotNull(message = "NFT type id cannot be empty")
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
	@NotBlank(message = "Nft type code cannot be empty")
	private String code;

	/**
	 * 类别名称
	 */
	@ApiModelProperty("类别名称")
	@NotBlank(message = "Nft type name cannot be empty")
	private String name;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer sort;

}