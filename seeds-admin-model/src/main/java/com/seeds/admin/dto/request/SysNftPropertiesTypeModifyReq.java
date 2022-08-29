package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 系统NFT属性类别信息
 * 
 * @author hang.yu
 * @date 2022/8/13
 */
@Data
@ApiModel(value = "SysNftPropertiesTypeModifyReq", description = "系统NFT属性类别信息")
public class SysNftPropertiesTypeModifyReq {

	/**
	 * NFT属性类别id
	 */
	@ApiModelProperty("NFT属性类别id")
	@NotNull(message = "NFT properties type id cannot be empty")
	private Long id;

	/**
	 * NFT属性类别code
	 */
	@ApiModelProperty("NFT名称")
	@NotBlank(message = "NFT properties type code cannot be empty")
	private String code;

	/**
	 * NFT属性类别名称
	 */
	@ApiModelProperty("NFT名称")
	@NotBlank(message = "NFT properties type name cannot be empty")
	private String name;


}