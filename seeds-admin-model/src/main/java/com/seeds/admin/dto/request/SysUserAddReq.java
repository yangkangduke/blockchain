package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统用户
 * 
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "系统用户信息")
public class SysUserAddReq {

	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	@NotBlank(message = "Account cannot be empty")
	private String account;

	/**
	 * 姓名
	 */
	@ApiModelProperty("姓名")
	private String realName;

	/**
	 * 初始密码
	 */
	@ApiModelProperty("初始密码")
	private String initPassport;

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String headUrl;

	/**
	 * 性别   0：男   1：女    2：保密
	 */
	@ApiModelProperty("性别   0：男   1：女    2：保密")
	private Integer gender;

	/**
	 * 邮箱
	 */
	@ApiModelProperty("邮箱")
	private String email;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String mobile;

	/**
	 * 状态  0：停用   1：正常
	 */
	@ApiModelProperty("状态  0：停用   1：正常")
	@NotNull(message = "User status cannot be empty")
	private Integer status;

	/**
	 * 部门ID
	 */
	@ApiModelProperty("部门ID")
	private Long deptId;

}