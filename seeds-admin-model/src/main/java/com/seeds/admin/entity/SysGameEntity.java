package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 游戏
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_game")
public class SysGameEntity extends BaseEntity {

	/**
	 * 游戏名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 游戏地址
	 */
	@TableField("url")
	private String url;

	/**
	 * 游戏状态  0：下架   1：正常
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 删除标记  1：已删除   0：未删除
	 */
	@TableLogic
	@TableField("delete_flag")
	private Integer deleteFlag;

}