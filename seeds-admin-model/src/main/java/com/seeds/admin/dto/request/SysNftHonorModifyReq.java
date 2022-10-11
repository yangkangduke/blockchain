package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统NFT战绩记录信息
 * 
 * @author hang.yu
 * @date 2022/10/08
 */
@Data
@ApiModel(value = "SysNftHonorModifyReq", description = "系统NFT战绩记录信息")
public class SysNftHonorModifyReq {

	/**
	 * NFT的id
	 */
	@ApiModelProperty("NFT的id")
	@NotNull(message = "NFT id cannot be empty")
	private Long nftId;

	/**
	 * 获胜次数
	 */
	@ApiModelProperty("获胜次数")
	@NotNull(message = "Win times cannot be empty")
	private Long winTimes;

	/**
	 * 失败次数
	 */
	@ApiModelProperty("失败次数")
	@NotNull(message = "Lose times cannot be empty")
	private Long loseTimes;

	/**
	 * 平局次数
	 */
	@ApiModelProperty("平局次数")
	@NotNull(message = "Tie times cannot be empty")
	private Long tieTimes;

	/**
	 * 连胜场数
	 */
	@ApiModelProperty("最大连胜场数")
	@NotNull(message = "Win streak cannot be empty")
	private Long winStreak;

	/**
	 * 连败场数
	 */
	@ApiModelProperty("最大连败场数")
	@NotNull(message = "Lose streak cannot be empty")
	private Long loseStreak;

	/**
	 * 击杀玩家次数
	 */
	@ApiModelProperty("击杀玩家次数")
	@NotNull(message = "Player kills cannot be empty")
	private Long playerKills;

	/**
	 * 连杀玩家次数
	 */
	@ApiModelProperty("最大连杀玩家次数")
	@NotNull(message = "Player kills row cannot be empty")
	private Long playerKillsRow;

	/**
	 * 击杀NPC次数
	 */
	@ApiModelProperty("击杀NPC次数")
	@NotNull(message = "Npc kills cannot be empty")
	private Long npcKills;

	/**
	 * 被玩家击杀次数
	 */
	@ApiModelProperty("被玩家击杀次数")
	@NotNull(message = "Kills by players cannot be empty")
	private Long killsByPlayers;

	/**
	 * 被NPC击杀次数
	 */
	@ApiModelProperty("被NPC击杀次数")
	@NotNull(message = "Kills by npc cannot be empty")
	private Long killsByNpc;

	/**
	 * 租借信息
	 */
	@ApiModelProperty("租借信息")
	@NotNull(message = "Leasing time cannot be empty")
	private Long leasingTime;

	/**
	 * 触发事件列表
	 */
	@ApiModelProperty("触发事件列表")
	@Valid
	private List<SysNftEventReq> eventList;

}