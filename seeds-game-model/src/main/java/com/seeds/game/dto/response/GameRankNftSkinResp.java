package com.seeds.game.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 游戏NFT皮肤
 * 
 * @author hang.yu
 * @date 2023/5/22
 */
@Data
@ApiModel(value = "GameRankNftSkinResp", description = "游戏NFT皮肤")
public class GameRankNftSkinResp {

	@ApiModelProperty(value = "过期时间")
	private Long expireTime;

	@ApiModelProperty(value = "排行榜数据")
	private List<GameRankNftSkin> infos;

	@Data
	@ApiModel(value = "GameRankNftSkin", description = "排行榜数据")
	public static class GameRankNftSkin {

		@ApiModelProperty("皮肤名称")
		private String name;

		@ApiModelProperty("图片")
		private String image;

		@ApiModelProperty("趋势 0 无 1 上升 2 下降")
		private Integer trend = 0;

		@ApiModelProperty("编号")
		private String number;

		@ApiModelProperty("公共地址")
		private String publicAddress;

		@ApiModelProperty("拥有者")
		private String collector;

		@ApiModelProperty("英雄职业")
		private String occupation;

		@ApiModelProperty("积分")
		private Integer score;

		@JsonSerialize(using= ToStringSerializer.class)
		private Long nftId;

		private Integer rank;

	}

}