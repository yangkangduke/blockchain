package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 系统NFT属性类别信息
 * 
 * @author hang.yu
 * @date 2022/8/13
 */
@Data
@ApiModel(value = "SysNftPropertiesTypeAddReq", description = "系统NFT属性类别信息")
public class SysNftPropertiesTypeAddReq {

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