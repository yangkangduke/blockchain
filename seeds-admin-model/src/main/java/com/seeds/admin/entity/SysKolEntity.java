package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * KOL管理
 * @author hang.yu
 * @date 2023/4/26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_kol")
public class SysKolEntity extends BaseEntity {

	/**
	 * KOL名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 邮箱
	 */
	@TableField("email")
	private String email;

	/**
	 * 备注
	 */
	@TableField("memo")
	private String memo;

	/**
	 * 邀请链接
	 */
	@TableField("invite_url")
	private String inviteUrl;

	/**
	 * 邀请编号
	 */
	@TableField("invite_no")
	private String inviteNo;

	/**
	 * 邀请码
	 */
	@TableField("invite_code")
	private String inviteCode;

	/**
	 * 状态  0：禁用   1：启用
	 */
	@TableField("status")
	private Integer status;

}