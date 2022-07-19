package com.seeds.admin.entity.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.admin.entity.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商家
 * 
 * @author hang.yu
 * @date 2022/7/19
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_merchant")
public class SysMerchantEntity extends BaseEntity {

	/**
	 * 商家名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 负责人id
	 */
	@TableField("leader_id")
	private Long leaderId;

	/**
	 * 联系方式
	 */
	@TableField("mobile")
	private String mobile;

	/**
	 * 商家状态  0：停用   1：正常
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 网站地址
	 */
	@TableField("url")
	private String url;

}