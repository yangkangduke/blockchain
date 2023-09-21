package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * NFT属性
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_nft_properties")
public class SysNftPropertiesEntity extends BaseEntity {

	/**
	 * NFT的id
	 */
	@TableField("nft_id")
	private Long nftId;

	/**
	 * NFT属性类别id
	 */
	@TableField("type_id")
	private Long typeId;

	/**
	 * NFT属性名称
	 */
	@TableField("name")
	private String name;

	/**
	 * NFT属性值
	 */
	@TableField("value")
	private String value;

}