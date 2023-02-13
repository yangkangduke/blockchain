package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


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

	@ApiModelProperty("战斗模式：317 Free，402 Loot")
	@NotNull(message = "mModeId cannot be empty")
	private Integer mModeId;

	@ApiModelProperty("玩家单局中击杀其他玩家的数量")
	@NotNull(message = "mKillNum cannot be empty")
	private Integer mKillNum;

	@ApiModelProperty("每局结束时玩家的生存时间")
	@NotNull(message = "mSurvivalTime cannot be empty")
	private Long mSurvivalTime;

	@ApiModelProperty("统计玩家当前的竞技场积分")
	@NotNull(message = "mTotalScore cannot be empty")
	private Long mTotalScore;

	@ApiModelProperty("玩家账号id")
	@NotBlank(message = "accId cannot be empty")
	private String accId;

	@ApiModelProperty("游戏服id")
	@NotBlank(message = "gameServerId cannot be empty")
	private String gameServerId;

	@ApiModelProperty("LootMode中获取图纸（仅Loot模式下有该参数）")
	private List<MatchDrawing> mDrawings;

	@Data
	@ApiModel(value = "MatchDrawing", description = "对局图纸")
	public static class MatchDrawing {

		@ApiModelProperty(value = "图纸id")
		private Long drawingId;

		@ApiModelProperty(value = "nft全局id")
		private Long autoId;

	}

}