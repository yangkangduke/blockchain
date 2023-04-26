package com.seeds.admin.dto.response;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 游戏排行榜导出明细
 *
 * @author hang.yu
 * @date 2023/04/26
 */
@Data
@ApiModel(value = "GaWinRankExportResp", description = "游戏排行榜导出明细")
public class GaWinRankExportResp {

	@ApiModelProperty(value = "玩家游戏id")
	@ExcelProperty(value = "玩家编号")
	private String accId;

	@ApiModelProperty(value = "玩家账号")
	@ExcelProperty(value = "玩家账号")
	private String accName;

	@ApiModelProperty(value = "区服名称")
	@ExcelProperty(value = "区服名称")
	private String regionName;

	@ApiModelProperty(value = "玩家昵称")
	@ExcelProperty(value = "玩家昵称")
	private String nickName;

	@ApiModelProperty(value = "总场数")
	@ExcelProperty(value = "总场数")
	private Long fightNum;

	@ApiModelProperty(value = "最高连胜场数")
	@ExcelProperty(value = "最高连胜场数")
	private Long maxSeqWin;

	@ApiModelProperty(value = "总积分")
	@ExcelProperty(value = "总积分")
	private Long score;

	@ApiModelProperty(value = "总胜利场数")
	@ExcelProperty(value = "总胜利场数")
	private Long winNum;

}