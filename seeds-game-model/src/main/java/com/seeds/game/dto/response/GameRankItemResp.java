package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 游戏道具排行
 * 
 * @author hang.yu
 * @date 2023/5/18
 */
@Data
@ApiModel(value = "GameRankItemResp", description = "游戏道具排行")
public class GameRankItemResp {

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

	@ApiModelProperty("成交时间")
	private Long fulfillTime;

	@ApiModelProperty("是否是拥有者，0 否 1 是")
	private Integer isOwner = 0;

}