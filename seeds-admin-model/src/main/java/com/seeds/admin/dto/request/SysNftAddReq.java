package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


/**
 * 系统NFT信息
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "SysNftAddReq", description = "系统NFT信息")
public class SysNftAddReq {

	/**
	 * NFT名称
	 */
	@ApiModelProperty("NFT名称")
	@NotBlank(message = "NFT name cannot be empty")
	private String name;

	/**
	 * 游戏id
	 */
	@ApiModelProperty("游戏id")
	@NotNull(message = "Game id cannot be empty")
	private Long gameId;

	/**
	 * NFT类别id
	 */
	@ApiModelProperty("NFT类别id")
	@NotNull(message = "NFT type id cannot be empty")
	private Long nftTypeId;

	/**
	 * NFT描述
	 */
	@ApiModelProperty("NFT描述")
	@NotBlank(message = "NFT description cannot be empty")
	private String description;

	/**
	 * NFT价格
	 */
	@ApiModelProperty("NFT价格")
	@NotNull(message = "NFT price cannot be empty")
	private BigDecimal price;

	/**
	 * 单位 USDC
	 */
	@ApiModelProperty("NFT价格单位")
	@NotBlank(message = "NFT price unit cannot be empty")
	private String unit;

	/**
	 * NFT是否在售  0：否   1：是
	 */
	@ApiModelProperty("NFT是否在售  0：否   1：是")
	@NotNull(message = "NFT status cannot be empty")
	private Integer status;

	/**
	 * NFT属性列表
	 */
	@Valid
	@ApiModelProperty("NFT属性列表")
	private List<NftPropertiesReq> propertiesList;

}