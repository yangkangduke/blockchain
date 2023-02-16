package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 单英雄记录
 *
 * @author hang.yu
 * @date 2023/02/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OpenHeroRecordReq", description = "单英雄记录")
public class OpenHeroRecordReq extends OpenSignReq {

	@ApiModelProperty(value = "英雄id")
	@NotNull(message = "heroId cannot be empty")
	private Long heroId;

	@ApiModelProperty(value = "总场次")
	@NotNull(message = "fightNum cannot be empty")
	private Long fightNum;

	@ApiModelProperty(value = "获胜场次")
	@NotNull(message = "winNum cannot be empty")
	private Long winNum;

	@ApiModelProperty("天赋等级")
	@NotNull(message = "heroLvl cannot be empty")
	private Integer heroLvl;

	@ApiModelProperty(value = "击杀其他玩家次数")
	@NotNull(message = "killNum cannot be empty")
	private Long killNum;

	@ApiModelProperty(value = "英雄总分（排名时使用）")
	@NotNull(message = "score cannot be empty")
	private Long score;

	@ApiModelProperty("玩家账号id")
	@NotBlank(message = "accID cannot be empty")
	private String accID;

	@ApiModelProperty("游戏服id")
	@NotBlank(message = "gameServerId cannot be empty")
	private String gameServerId;

}