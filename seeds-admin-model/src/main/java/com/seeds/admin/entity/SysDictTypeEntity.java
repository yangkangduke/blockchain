package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据字典类型
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_dict_type")
public class SysDictTypeEntity extends BaseEntity {

	/**
	 * 字典编码
	 */
	@TableField("dict_code")
	private String dictCode;

	/**
	 * 字典名称
	 */
	@TableField("dict_name")
	private String dictName;

	/**
	 * 父级编码
	 */
	@TableField("parent_code")
	private String parentCode;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

}