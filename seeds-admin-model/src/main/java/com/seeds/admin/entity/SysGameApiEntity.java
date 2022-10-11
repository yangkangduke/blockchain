package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 游戏api
 * 
 * @author hang.yu
 * @date 2022/10/10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_game_api")
public class SysGameApiEntity extends BaseEntity {

	/**
	 * 游戏id
	 */
	@TableField("game_id")
	private Long gameId;

	/**
	 * 游戏api类型
	 */
	@TableField("type")
	private Integer type;

	/**
	 * 游戏api描述
	 */
	@TableField("`desc`")
	private String desc;

	/**
	 * 游戏api
	 */
	@TableField("api")
	private String api;

	/**
	 * 游戏基础url
	 */
	@TableField("base_url")
	private String baseUrl;

}