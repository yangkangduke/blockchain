package com.seeds.admin.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.admin.entity.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 系统角色
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_role")
public class SysRoleEntity extends BaseEntity {

	/**
	 * 角色编码
	 */
	@TableField("role_code")
	private String roleCode;

	/**
	 * 角色名称
	 */
	@TableField("role_name")
	private String roleName;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 删除标记  1：已删除   0：未删除
	 */
	@TableField("delete_flag")
	private Integer deleteFlag;

}