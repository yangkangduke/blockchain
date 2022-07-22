package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 系统游戏信息
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "系统游戏信息")
public class SysGameModifyReq {

	/**
	 * 游戏id
	 */
	@ApiModelProperty("游戏id")
	@NotNull(message = "Game id cannot be empty")
	private Long id;

	/**
	 * 游戏名称
	 */
	@ApiModelProperty("游戏名称")
	@NotBlank(message = "Game name cannot be empty")
	private String name;

	/**
	 * 游戏地址
	 */
	@ApiModelProperty("游戏地址")
	@NotBlank(message = "Game url cannot be empty")
	private String url;

}