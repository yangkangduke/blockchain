package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * NFT类别
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_nft_type")
public class SysNftTypeEntity extends BaseEntity {

	/**
	 * 父类别code
	 */
	@TableField("parent_code")
	private String parentCode;

	/**
	 * 类别编码
	 */
	@TableField("code")
	private String code;

	/**
	 * 类别名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

	/**
	 * 是否有效  0：否   1：是
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 删除标记  0：未删除
	 */
	@TableLogic(value = "0", delval = "NULL")
	@TableField("delete_flag")
	private Integer deleteFlag;

	/**
	 * 上级类别名称
	 */
	@TableField(exist = false)
	private String parentName;

}