package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 游戏胜场排行统计
 * 
 * @author hang.yu
 * @date 2023/5/11
 */
@Data
@ApiModel(value = "GameRankStatisticResp", description = "游戏胜场排行统计")
public class GameRankStatisticResp {

	@ApiModelProperty("游戏角色名称")
	private String roleName;

	@ApiModelProperty("天梯积分")
	private BigDecimal ladderScore;

	@ApiModelProperty("胜率")
	private String winRate;

	@ApiModelProperty("最大连胜")
	private Integer seqWinNum;

	@ApiModelProperty("总场次")
	private Long fightNum;

	@ApiModelProperty("获胜场次")
	private Long winNum;

	@ApiModelProperty("最大击杀数")
	private Integer maxKillNum;

	@ApiModelProperty("头像链接")
	private String portraitUrl;

}