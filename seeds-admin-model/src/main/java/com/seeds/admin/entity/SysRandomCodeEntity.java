package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
	 * 随机码
	 */
	@TableField("code")
	private String code;

	/**
	 * 长度
	 */
	@TableField("length")
	private Integer length;

	/**
	 * 使用标记 0：未使用 1：已使用
	 */
	@TableField("use_flag")
	private Integer useFlag;

	/**
	 * 描述
	 */
	@TableField("`desc`")
	private String desc;

	/**
	 * 过期时间
	 */
	@ApiModelProperty("过期时间")
	private Long expireTime;

}