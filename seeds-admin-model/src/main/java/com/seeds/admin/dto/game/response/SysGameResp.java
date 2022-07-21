package com.seeds.admin.dto.game.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统游戏
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "系统游戏信息")
public class SysGameResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "游戏名称")
	private String name;

	@ApiModelProperty(value = "游戏地址")
	private String url;

	@ApiModelProperty(value = "游戏状态  0：下架   1：正常")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

}