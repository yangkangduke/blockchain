package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 系统商家信息
 * 
 * @author hang.yu
 * @date 2022/7/19
 */
@Data
@ApiModel(value = "SysMerchantAddReq", description = "系统商家信息")
public class SysMerchantAddReq {

	/**
	 * 商家名称
	 */
	@ApiModelProperty("商家名称")
	@NotBlank(message = "Merchant name cannot be empty")
	private String name;

	/**
	 * 负责人id
	 */
	@ApiModelProperty("负责人id")
	private Long leaderId;

	/**
	 * 负责人姓名
	 */
	@ApiModelProperty("负责人姓名")
	@NotBlank(message = "Leader name cannot be empty")
	private String leaderName;

	/**
	 * 联系方式
	 */
	@ApiModelProperty("联系方式")
	@NotBlank(message = "Contact number cannot be empty")
	private String mobile;

	/**
	 * 网址
	 */
	@ApiModelProperty("网址")
	private String url;

	/**
	 * 状态  0：停用   1：正常
	 */
	@ApiModelProperty("状态  0：停用   1：正常")
	@NotNull(message = "Merchant status cannot be empty")
	private Integer status;

}