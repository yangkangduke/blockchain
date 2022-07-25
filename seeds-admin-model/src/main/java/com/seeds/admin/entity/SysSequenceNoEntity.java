package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 系统序列号
 * 
 * @author hang.yu
 * @date 2022/7/25
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_sequence_no")
public class SysSequenceNoEntity extends BaseEntity {

	/**
	 * 类型
	 */
	@TableField("type")
	private String type;

	/**
	 * 前缀
	 */
	@TableField("prefix")
	private String prefix;

	/**
	 * 号码
	 */
	@TableField("number")
	private Long number;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;


}