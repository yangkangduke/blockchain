package com.seeds.admin.dto.sys.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 系统用户
 * 
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "系统用户信息")
public class SysUserResp {

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	private Long id;

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
	 * 角色
	 */
	@ApiModelProperty("角色")
	private String roleNameStr;

	/**
	 * 角色id列表
	 */
	@ApiModelProperty("角色id列表")
	private List<Long> roleIds;

	/**
	 * 部门ID
	 */
	@ApiModelProperty("部门ID")
	private Long deptId;

	/**
	 * 状态  0：停用   1：正常
	 */
	@ApiModelProperty("状态  0：停用   1：正常")
	private Integer status;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Long createdAt;

	/**
	 * 删除标记  1：已删除   0：未删除
	 */
	@ApiModelProperty("删除标记  1：已删除   0：未删除")
	private Integer deleteFlag;

}