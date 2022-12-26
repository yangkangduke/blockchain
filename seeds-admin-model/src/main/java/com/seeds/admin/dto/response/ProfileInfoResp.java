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

	@ApiModelProperty(value = "过期时间")
	private Long expireTime;

	@ApiModelProperty(value = "胜利总场次")
	private Long winNum = 0L;

	@ApiModelProperty(value = "最大连胜场次")
	private Long maxSeqWin = 0L;

	@ApiModelProperty(value = "战斗总场次")
	private Long fightNum = 0L;

	@ApiModelProperty(value = "总积分")
	private Long score = 0L;

	@ApiModelProperty(value = "排名，-1表示未上榜")
	private Long rank;

	@ApiModelProperty(value = "英雄记录信息")
	private List<GameHeroRecord> heroRecord;

	@Data
	@ApiModel(value = "GameHeroRecord", description = "英雄记录信息")
	public static class GameHeroRecord {

		@ApiModelProperty(value = "胜利总数")
		private Long tw = 0L;

		@ApiModelProperty(value = "最大连杀数")
		private Long msk = 0L;

		@ApiModelProperty(value = "最多救援")
		private Long ms = 0L;

		@ApiModelProperty(value = "英雄使用次数")
		private Long num = 0L;

		@ApiModelProperty(value = "3S次数")
		private Long tsss = 0L;

		@ApiModelProperty(value = "最大得分")
		private Long mts = 0L;

		@ApiModelProperty(value = "总收集")
		private Long tc = 0L;

		@ApiModelProperty(value = "前几名总次数")
		private Long ttn = 0L;

		@ApiModelProperty(value = "最长存活时间")
		private Long mst = 0L;

		@ApiModelProperty(value = "死亡总数")
		private Long td = 0L;

		@ApiModelProperty(value = "总存活时间")
		private Long tst = 0L;

		@ApiModelProperty(value = "最多清兵数")
		private Long mks = 0L;

		@ApiModelProperty(value = "最大收集")
		private Long mc = 0L;

		@ApiModelProperty(value = "击杀总数")
		private Long tk = 0L;

		@ApiModelProperty(value = "英雄id")
		private Long id;

		@ApiModelProperty(value = "总清兵数")
		private Long tks = 0L;

		@ApiModelProperty(value = "总救援")
		private Long ts = 0L;

		@ApiModelProperty(value = "英雄名字")
		private String nm;

		@ApiModelProperty(value = "英雄类型")
		private Long ty;

		@ApiModelProperty(value = "英雄最大连胜次数")
		private Long msw = 0L;

		@ApiModelProperty(value = "英雄连胜次数")
		private Long sw = 0L;

		@ApiModelProperty(value = "比赛胜率（胜场数/总场数）")
		private String winRate;

	}

}