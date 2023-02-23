package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * 对局记录
 *
 * @author hang.yu
 * @date 2023/02/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OpenMatchRecordReq", description = "对局记录")
public class OpenMatchRecordReq extends OpenSignReq {

	@ApiModelProperty("击杀总数")
	@NotNull(message = "totalKillNum cannot be empty")
	private Long totalKillNum;

	@ApiModelProperty("最大击杀数")
	private Integer maxKillNum;

	@ApiModelProperty("血腥积分")
	@NotNull(message = "killScore cannot be empty")
	private BigDecimal killScore;

	@ApiModelProperty("总生存时间")
	@NotNull(message = "totalSurvivalTime cannot be empty")
	private Long totalSurvivalTime;

	@ApiModelProperty("最大生存时间")
	private Long maxSurvivalTime;

	@ApiModelProperty("生存积分")
	@NotNull(message = "survivalScore cannot be empty")
	private BigDecimal survivalScore;

	@ApiModelProperty("生产积分")
	private BigDecimal drawingScore;

	@ApiModelProperty("lootMode次数")
	private Long lootFightNum;

	@ApiModelProperty("lootMode获取图纸数量")
	private Long lootDrawNum;

	@ApiModelProperty("掠夺积分")
	@NotNull(message = "lootScore cannot be empty")
	private BigDecimal lootScore;

	@ApiModelProperty("天梯积分")
	private BigDecimal ladderScore;

	@ApiModelProperty("竞技场积分")
	@NotNull(message = "rankScore cannot be empty")
	private BigDecimal rankScore;

	@ApiModelProperty("获胜场次")
	@NotNull(message = "winNum cannot be empty")
	private Long winNum;

	@ApiModelProperty("总场次")
	@NotNull(message = "fightNum cannot be empty")
	private Long fightNum;

	@ApiModelProperty("胜率")
	@NotNull(message = "winRate cannot be empty")
	private BigDecimal winRate;

	@ApiModelProperty("最大连胜")
	@NotNull(message = "seqWinNum cannot be empty")
	private Integer seqWinNum;

	@ApiModelProperty("综合积分")
	@NotNull(message = "comprehensiveScore cannot be empty")
	private BigDecimal comprehensiveScore;

	@ApiModelProperty("玩家账号id")
	@NotBlank(message = "accID cannot be empty")
	private String accID;

	@ApiModelProperty("游戏服id")
	@NotBlank(message = "gameServerId cannot be empty")
	private String gameServerId;

}