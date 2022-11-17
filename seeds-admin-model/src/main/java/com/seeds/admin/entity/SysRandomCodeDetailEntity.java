package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 随机码明细
 * 
 * @author hang.yu
 * @date 2022/11/07
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_random_code_detail")
public class SysRandomCodeDetailEntity extends BaseEntity {

	/**
	 * 批次号
	 */
	@TableField("batch_no")
	private String batchNo;

	 /**
	 * 随机码
	 */
	@TableField("code")
	private String code;

	/**
	 * 关联用户标识
	 */
	@TableField("user_identity")
	private String userIdentity;

	/**
	 * 使用标记 0：未使用 1：已使用
	 */
	@TableField("use_flag")
	private Integer useFlag;

}