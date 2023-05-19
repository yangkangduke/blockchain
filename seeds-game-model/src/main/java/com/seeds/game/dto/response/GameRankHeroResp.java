package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 游戏英雄排行
 * 
 * @author hang.yu
 * @date 2023/5/18
 */
@Data
@ApiModel(value = "GameRankHeroResp", description = "游戏英雄排行")
public class GameRankHeroResp {

	@ApiModelProperty("装备名称")
	private String name;

	@ApiModelProperty("图片")
	private String image;

	@ApiModelProperty("编号")
	private String number;

	@ApiModelProperty("公共地址")
	private String publicAddress;

	@ApiModelProperty("拥有者")
	private String collector;

	@ApiModelProperty("价格")
	private BigDecimal price;

	@ApiModelProperty("浏览量")
	private Integer views;

	@ApiModelProperty("获胜次数")
	private Integer victory;

	@ApiModelProperty("失败次数")
	private Integer lose;

	@ApiModelProperty("最大连胜场数")
	private Integer maxStreak;

	@ApiModelProperty("击杀玩家次数")
	private Integer capture;

	@ApiModelProperty("最大连杀数")
	private Integer killingSpree;

	@ApiModelProperty("击杀NPC数")
	private Integer goblinKill;

	@ApiModelProperty("被玩家击杀数")
	private Integer slaying;

	@ApiModelProperty("成交时间")
	private Long fulfillTime;

}