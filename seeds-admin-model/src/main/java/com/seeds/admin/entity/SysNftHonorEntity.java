package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * NFT战绩
 * 
 * @author hang.yu
 * @date 2022/10/8
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_nft_honor")
public class SysNftHonorEntity extends BaseEntity {

	/**
	 * NFT的id
	 */
	@TableField("nft_id")
	private Long nftId;

	/**
	 * 获胜次数
	 */
	@TableField("win_times")
	private Long winTimes;

	/**
	 * 失败次数
	 */
	@TableField("lose_times")
	private Long loseTimes;

	/**
	 * 平局次数
	 */
	@TableField("tie_times")
	private Long tieTimes;

	/**
	 * 连胜场数
	 */
	@TableField("win_streak")
	private Long winStreak;

	/**
	 * 连败场数
	 */
	@TableField("lose_streak")
	private Long loseStreak;

	/**
	 * 击杀玩家次数
	 */
	@TableField("player_kills")
	private Long playerKills;

	/**
	 * 连杀玩家次数
	 */
	@TableField("player_kills_row")
	private Long playerKillsRow;

	/**
	 * 击杀NPC次数
	 */
	@TableField("npc_kills")
	private Long npcKills;

	/**
	 * 被玩家击杀次数
	 */
	@TableField("kills_by_players")
	private Long killsByPlayers;

	/**
	 * 被NPC击杀次数
	 */
	@TableField("kills_by_npc")
	private Long killsByNpc;

	/**
	 * 租借信息
	 */
	@TableField("leasing_time")
	private Long leasingTime;

}