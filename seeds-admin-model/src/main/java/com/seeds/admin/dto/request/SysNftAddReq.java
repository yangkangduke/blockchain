package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;


/**
 * 系统NFT信息
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "系统NFT信息")
public class SysNftAddReq {

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
	@NotEmpty(message = "NFT type id cannot be empty")
	private Set<Long> nftTypeId;

	/**
	 * NFT图片
	 */
	@ApiModelProperty("NFT图片")
	@NotBlank(message = "NFT picture cannot be empty")
	private String picture;

	/**
	 * NFT归属人
	 */
	@ApiModelProperty("NFT归属人")
	@NotBlank(message = "NFT owner cannot be empty")
	private String owner;

	/**
	 * NFT价格
	 */
	@ApiModelProperty("NFT价格")
	@NotNull(message = "NFT price cannot be empty")
	private BigDecimal price;

	/**
	 * NFT是否在售  0：否   1：是
	 */
	@ApiModelProperty("NFT是否在售  0：否   1：是")
	@NotNull(message = "NFT status cannot be empty")
	private Integer status;

}