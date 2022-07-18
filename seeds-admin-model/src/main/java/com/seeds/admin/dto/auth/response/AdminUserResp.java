package com.seeds.admin.dto.auth.response;

import com.seeds.admin.dto.sys.response.SysMenuResp;
import com.seeds.admin.dto.sys.response.SysRoleResp;
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
@ApiModel(value = "登录用户信息")
public class AdminUserResp {

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

	/**
	 * 超级管理员   0：否   1：是
	 */
	@ApiModelProperty("超级管理员   0：否   1：是")
	private Integer superAdmin;

	/**
	 * 角色列表
	 */
	@ApiModelProperty(value = "角色列表")
	private List<SysRoleResp> roleList;

	/**
	 * 菜单列表
	 */
	@ApiModelProperty(value = "菜单列表")
	private List<SysMenuResp> menuList;

}