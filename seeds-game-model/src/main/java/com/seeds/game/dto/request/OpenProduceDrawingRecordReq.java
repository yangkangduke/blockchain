package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 产生图纸记录
 *
 * @author hang.yu
 * @date 2023/02/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OpenProduceDrawingRecordReq", description = "产生图纸记录")
public class OpenProduceDrawingRecordReq extends OpenSignReq {

	@ApiModelProperty(value = "图纸id")
	@NotNull(message = "drawingId cannot be empty")
	private Long drawingId;

	@ApiModelProperty("等级")
	@NotNull(message = "level cannot be empty")
	private Integer level;

	@ApiModelProperty(value = "nft全局id（仅nft道具有该字段）")
	private Long autoId;

	@ApiModelProperty("耐久")
	private Integer durability;

	@ApiModelProperty("玩家账号id")
	@NotBlank(message = "accId cannot be empty")
	private String accId;

	@ApiModelProperty("游戏服id")
	@NotBlank(message = "gameServerId cannot be empty")
	private String gameServerId;

}