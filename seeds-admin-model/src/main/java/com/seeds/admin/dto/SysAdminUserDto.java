package com.seeds.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统用户
 * 
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "系统用户信息")
public class SysAdminUserDto {

	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
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
	 * 部门ID
	 */
	@ApiModelProperty("部门ID")
	private Long deptId;

	/**
	 * 超级管理员   0：否   1：是
	 */
	@ApiModelProperty("超级管理员   0：否   1：是")
	private Integer superAdmin;

	/**
	 * 状态  0：停用   1：正常
	 */
	@ApiModelProperty("状态  0：停用   1：正常")
	private Integer status;

	/**
	 * 删除标记  0：已删除   1：未删除
	 */
	@ApiModelProperty("删除标记  0：已删除   1：未删除")
	private Integer deleteFlag;

}