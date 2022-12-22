package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 用户个人概括信息
 * 
 * @author hang.yu
 * @date 2022/12/22
 */
@Data
@ApiModel(value = "UcProfileInfoResp", description = "用户个人概括信息")
public class ProfileInfoResp {

	@ApiModelProperty(value = "请求状态")
	private String ret;

	@ApiModelProperty(value = "胜利总场次")
	private Long winNum;

	@ApiModelProperty(value = "最大连胜场次")
	private Long maxSeqWin;

	@ApiModelProperty(value = "战斗总场次")
	private Long fightNum;

	@ApiModelProperty(value = "英雄记录信息")
	private List<GameHeroRecord> heroRecord;

	@Data
	@ApiModel(value = "GameHeroRecord", description = "英雄记录信息")
	public static class GameHeroRecord {

		@ApiModelProperty(value = "比赛胜率（胜场数/总场数）")
		private Long winRate;

	}

}