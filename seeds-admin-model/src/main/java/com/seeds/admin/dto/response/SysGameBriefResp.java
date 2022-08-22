package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统游戏简略信息
 * 
 * @author hang.yu
 * @date 2022/8/17
 */
@Data
@ApiModel(value = "SysGameBriefResp", description = "系统游戏简略信息")
public class SysGameBriefResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "游戏名称")
	private String name;

	@ApiModelProperty("开发者")
	private String developer;

}