package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商家与用户关联
 * 
 * @author hang.yu
 * @date 2022/7/20
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_merchant_user")
public class SysMerchantUserEntity extends BaseEntity {

	/**
	 * 商家ID
	 */
	@TableField("merchant_id")
	private Long merchantId;

	/**
	 * 用户ID
	 */
	@TableField("user_id")
	private Long userId;

}