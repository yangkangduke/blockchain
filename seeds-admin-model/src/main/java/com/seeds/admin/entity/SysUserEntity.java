package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class SysUserEntity extends BaseEntity {

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
	 * 头像链接
	 */
	@TableField("url")
	private String url;

	/**
	 * 头像文件id
	 */
	@TableField("file_id")
	private Long fileId;

	/**
	 * 性别   0：女  1：男  2：保密
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
	 * 删除标记  0：未删除
	 */
	@TableLogic(value = "0", delval = "NULL")
	@TableField("delete_flag")
	private Integer deleteFlag;

	/**
	 * 钱包地址
	 */
	@TableField("public_address")
	private String publicAddress;

	/**
	 * metamask标记  0：未启用  1：启用
	 */
	@TableField("metamask_flag")
	private Integer metamaskFlag;

	/**
	 * 随机数，metamask验证时使用
	 */
	@TableField("nonce")
	private String nonce;

}