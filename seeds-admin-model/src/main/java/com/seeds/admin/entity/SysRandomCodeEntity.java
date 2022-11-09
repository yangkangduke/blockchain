package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 随机码
 * 
 * @author hang.yu
 * @date 2022/11/07
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_random_code")
public class SysRandomCodeEntity extends BaseEntity {

	/**
	 * 批次号
	 */
	@TableField("batch_no")
	private String batchNo;

	/**
	 * 类型 1：邀请码
	 */
	@TableField("type")
	private Integer type;

	/**
	 * 长度
	 */
	@TableField("length")
	private Integer length;

	/**
	 * 数量
	 */
	@TableField("number")
	private Integer number;

	/**
	 * 状态  0：正常  1：生成中  2：生成失败  3：导出中  6：导出失败
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 表格地址
	 */
	@TableField("excel_url")
	private String excelUrl;

	/**
	 * 描述
	 */
	@TableField("`desc`")
	private String desc;

	/**
	 * 过期时间
	 */
	@TableField("expire_time")
	private Long expireTime;

}