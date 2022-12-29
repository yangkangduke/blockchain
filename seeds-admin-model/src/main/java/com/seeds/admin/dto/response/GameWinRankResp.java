package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 游戏胜场排行
 * 
 * @author hang.yu
 * @date 2022/12/12
 */
@Data
@ApiModel(value = "GameWinRankResp", description = "游戏胜场排行信息")
public class GameWinRankResp {

	@ApiModelProperty(value = "请求状态")
	private String ret;

	@ApiModelProperty(value = "过期时间")
	private Long expireTime;

	@ApiModelProperty(value = "排行榜数据")
	private List<GameWinRank> infos;

	@Data
	@ApiModel(value = "GameWinRank", description = "排行榜数据")
	public static class GameWinRank {

		@ApiModelProperty(value = "玩家游戏id")
		private Long accID;

		@ApiModelProperty(value = "玩家账号")
		private String accName;

		@ApiModelProperty(value = "总场数")
		private Long fightNum = 0L;

		@ApiModelProperty(value = "最高连胜场数")
		private Long maxSeqWin = 0L;

		@ApiModelProperty(value = "玩家昵称")
		private String nickName;

		@ApiModelProperty(value = "排行榜序号")
		private Long no;

		@ApiModelProperty(value = "头像id")
		private Long portraitId;

		@ApiModelProperty(value = "头像url")
		private String portraitUrl;

		@ApiModelProperty(value = "总积分")
		private Long score = 0L;

		@ApiModelProperty(value = "总胜利场数")
		private Long winNum = 0L;

	}

}