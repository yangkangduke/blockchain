package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 系统NFT信息
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "SysNftModifyReq", description = "系统NFT信息")
public class SysNftModifyReq {

	/**
	 * NFT的id
	 */
	@ApiModelProperty("NFT的id")
	@NotNull(message = "NFT id cannot be empty")
	private Long id;

	/**
	 * nft名称
	 */
	@ApiModelProperty("NFT名称")
	@NotBlank(message = "NFT name cannot be empty")
	private String name;

	/**
	 * NFT描述
	 */
	@ApiModelProperty("NFT描述")
	@NotBlank(message = "NFT description cannot be empty")
	private String description;

	/**
	 * NFT属性列表
	 */
	@Valid
	@ApiModelProperty("NFT属性列表")
	private List<NftPropertiesReq> propertiesList;

}