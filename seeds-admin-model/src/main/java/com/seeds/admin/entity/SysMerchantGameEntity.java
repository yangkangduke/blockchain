package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.admin.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商家与游戏关联
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_merchant_game")
public class SysMerchantGameEntity extends BaseEntity {

	/**
	 * 角色ID
	 */
	@TableField("merchant_id")
	private Long merchantId;

	/**
	 * 游戏ID
	 */
	@TableField("game_id")
	private Long gameId;

}