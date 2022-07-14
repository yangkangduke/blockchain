package com.seeds.admin.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.admin.entity.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户
 * 
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_user")
public class SysAdminUserEntity extends BaseEntity {

	/**
	 * 用户名
	 */
	@TableField("account")
	private String account;

	/**
	 * 密码
	 */
	@TableField("password")
	private String password;

	/**
	 * 姓名
	 */
	@TableField("real_name")
	private String realName;

	/**
	 * 头像
	 */
	@TableField("head_url")
	private String headUrl;

	/**
	 * 性别   0：男   1：女    2：保密
	 */
	@TableField("gender")
	private Integer gender;

	/**
	 * 邮箱
	 */
	@TableField("email")
	private String email;

	/**
	 * 手机号
	 */
	@TableField("mobile")
	private String mobile;

	/**
	 * 部门ID
	 */
	@TableField("dept_id")
	private Long deptId;

	/**
	 * 盐值
	 */
	@TableField("salt")
	private String salt;

	/**
	 * 超级管理员   0：否   1：是
	 */
	@TableField("super_admin")
	private Integer superAdmin;

	/**
	 * 状态  0：停用   1：正常
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 删除标记  0：已删除   1：未删除
	 */
	@TableField("delete_flag")
	private Integer deleteFlag;

}