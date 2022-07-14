package com.seeds.admin.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.admin.entity.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 系统角色与菜单对应关系
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_role_menu")
public class SysRoleMenuEntity extends BaseEntity {

	/**
	 * 角色ID
	 */
	@TableField("role_id")
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@TableField("menu_id")
	private Long menuId;

}