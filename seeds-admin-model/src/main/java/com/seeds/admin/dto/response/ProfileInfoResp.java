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

		@ApiModelProperty(value = "胜利总数")
		private Long tw;

		@ApiModelProperty(value = "最大连杀数")
		private Long msk;

		@ApiModelProperty(value = "最多救援")
		private Long ms;

		@ApiModelProperty(value = "英雄使用次数")
		private Long num;

		@ApiModelProperty(value = "3S次数")
		private Long tsss;

		@ApiModelProperty(value = "最大得分")
		private Long mts;

		@ApiModelProperty(value = "总收集")
		private Long tc;

		@ApiModelProperty(value = "前几名总次数")
		private Long ttn;

		@ApiModelProperty(value = "最长存活时间")
		private Long mst;

		@ApiModelProperty(value = "死亡总数")
		private Long td;

		@ApiModelProperty(value = "总存活时间")
		private Long tst;

		@ApiModelProperty(value = "最多清兵数")
		private Long mks;

		@ApiModelProperty(value = "最大收集")
		private Long mc;

		@ApiModelProperty(value = "击杀总数")
		private Long tk;

		@ApiModelProperty(value = "英雄id")
		private Long id;

		@ApiModelProperty(value = "总清兵数")
		private Long tks;

		@ApiModelProperty(value = "总救援")
		private Long ts;

		@ApiModelProperty(value = "比赛胜率（胜场数/总场数）")
		private String winRate;

	}

}