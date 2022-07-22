package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 系统商家用户信息
 * 
 * @author hang.yu
 * @date 2022/7/20
 */
@Data
@ApiModel(value = "系统商家用户信息")
public class SysMerchantUserAddReq {

	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	private Long userId;

	/**
	 * 用户姓名
	 */
	@ApiModelProperty("用户姓名")
	@NotBlank(message = "User name cannot be empty")
	private String userName;

	/**
	 * 联系方式
	 */
	@ApiModelProperty("联系方式")
	@NotBlank(message = "Contact number cannot be empty")
	private String mobile;

}