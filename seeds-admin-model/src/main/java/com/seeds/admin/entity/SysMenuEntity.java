package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 系统菜单
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_menu")
public class SysMenuEntity extends BaseEntity {

	/**
	 * 父菜单ID，一级菜单为0
	 */
	@TableField("parent_code")
	private String parentCode;

	/**
	 * 菜单编码
	 */
	@TableField("code")
	private String code;

	/**
	 * 菜单名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 菜单URL
	 */
	@TableField("url")
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：sys:user:list,sys:user:save)
	 */
	@TableField("permissions")
	private String permissions;

	/**
	 * 类型   0：菜单   1：按钮
	 */
	@TableField("type")
	private Integer type;

	/**
	 * 菜单图标
	 */
	@TableField("icon")
	private String icon;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

	/**
	 * 是否展示   0：否   1：是
	 */
	@TableField("show_flag")
	private Integer showFlag;

	/**
	 * 上级菜单名称
	 */
	@TableField(exist = false)
	private String parentName;

}