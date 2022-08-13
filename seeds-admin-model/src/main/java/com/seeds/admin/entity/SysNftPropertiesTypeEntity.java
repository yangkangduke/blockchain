package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * NFT属性类型
 * 
 * @author hang.yu
 * @date 2022/8/13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_nft_properties_type")
public class SysNftPropertiesTypeEntity extends BaseEntity {

	/**
	 * NFT属性类别code
	 */
	@TableField("code")
	private String code;

	/**
	 * NFT属性类别名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 删除标记  1：已删除   0：未删除
	 */
	@TableLogic
	@TableField("delete_flag")
	private Integer deleteFlag;

}