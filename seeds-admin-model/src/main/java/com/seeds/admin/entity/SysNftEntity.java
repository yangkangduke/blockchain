package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.admin.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * NFT
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_nft")
public class SysNftEntity extends BaseEntity {

	/**
	 * 编号
	 */
	@TableField("number")
	private String number;

	/**
	 * 图片
	 */
	@TableField("picture")
	private String picture;

	/**
	 * 名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 游戏id
	 */
	@TableField("game_id")
	private Long gameId;

	/**
	 * 价格
	 */
	@TableField("price")
	private BigDecimal price;

	/**
	 * 是否在售  0：否   1：是
	 */
	@TableField("status")
	private Integer status;


	/**
	 * 归属人
	 */
	@TableField("owner")
	private String owner;


	/**
	 * 删除标记  1：已删除   0：未删除
	 */
	@TableField("delete_flag")
	private Integer deleteFlag;

}