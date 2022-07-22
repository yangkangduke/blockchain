package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据字典内容
 * 
 * @author hang.yu
 * @date 2022/7/14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_dict_data")
public class SysDictDataEntity extends BaseEntity {

	/**
	 * 字典类型ID
	 */
	@TableField("dict_type_id")
	private Long dictTypeId;

	/**
	 * 字典标签
	 */
	@TableField("dict_label")
	private String dictLabel;

	/**
	 * 字典值
	 */
	@TableField("dict_value")
	private String dictValue;

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