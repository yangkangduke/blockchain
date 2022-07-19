package com.seeds.admin.dto.merchant.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 系统商家信息
 * 
 * @author hang.yu
 * @date 2022/7/19
 */
@Data
@ApiModel(value = "系统商家信息")
public class SysMerchantModifyReq {

	/**
	 * 商家id
	 */
	@ApiModelProperty("商家id")
	@NotBlank(message = "Merchant id cannot be empty")
	private String id;

	/**
	 * 商家名称
	 */
	@ApiModelProperty("商家名称")
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

}