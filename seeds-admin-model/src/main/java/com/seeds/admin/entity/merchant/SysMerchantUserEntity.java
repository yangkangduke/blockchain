package com.seeds.admin.entity.merchant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.admin.entity.common.BaseEntity;
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
	 * 角色ID
	 */
	@TableField("merchant_id")
	private Long merchantId;

	/**
	 * 菜单ID
	 */
	@TableField("user_id")
	private Long userId;

}