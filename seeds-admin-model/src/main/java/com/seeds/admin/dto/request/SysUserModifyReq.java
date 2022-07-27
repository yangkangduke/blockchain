package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 系统用户
 * 
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "SysUserModifyReq", description = "系统用户信息")
public class SysUserModifyReq {

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	@NotNull(message = "User id cannot be empty")
	private Long id;

	/**
	 * 姓名
	 */
	@ApiModelProperty("姓名")
	private String realName;

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String headUrl;

	/**
	 * 性别   0：女  1：男  2：保密
	 */
	@ApiModelProperty("性别   0：女  1：男  2：保密")
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
	 * 部门ID
	 */
	@ApiModelProperty("部门ID")
	private Long deptId;

}