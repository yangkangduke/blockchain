package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * 系统游戏信息
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "SysGameModifyReq", description = "系统游戏信息")
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
	 * 简介
	 */
	@ApiModelProperty("简介")
	@NotBlank(message = "brief cannot be empty")
	private String brief;

	/**
	 * 价格
	 */
	@ApiModelProperty("价格")
	@NotBlank(message = "price cannot be empty")
	private BigDecimal price;

	/**
	 * 单位 USDC
	 */
	@ApiModelProperty("单位 USDC")
	@NotBlank(message = "unit cannot be empty")
	private String unit;

	/**
	 * 官方网址
	 */
	@ApiModelProperty("官方网址")
	@NotBlank(message = "Official url cannot be empty")
	private String officialUrl;

	/**
	 * 下载地址
	 */
	@ApiModelProperty("下载地址")
	@NotBlank(message = "Download url cannot be empty")
	private String downloadUrl;

	/**
	 * 社区地址
	 */
	@ApiModelProperty("社区地址")
	@NotBlank(message = "Community url cannot be empty")
	private String communityUrl;

	/**
	 * 开发者
	 */
	@ApiModelProperty("开发者")
	@NotBlank(message = "Developer cannot be empty")
	private String developer;

	/**
	 * 图片链接
	 */
	@ApiModelProperty("图片链接")
	@NotBlank(message = "Picture url cannot be empty")
	private String pictureUrl;

	/**
	 * 图片文件id
	 */
	@ApiModelProperty("图片文件id")
	@NotNull(message = "Picture file id cannot be empty")
	private Long pictureFileId;

	/**
	 * 视频链接
	 */
	@ApiModelProperty("视频链接")
	@NotBlank(message = "Video url cannot be empty")
	private String videoUrl;

	/**
	 * 视频文件id
	 */
	@ApiModelProperty("视频文件id")
	@NotNull(message = "Video file id cannot be empty")
	private Long videoFileId;

	/**
	 * 是否允许评论  0：不允许   1：允许
	 */
	@ApiModelProperty("是否允许评论  0：不允许   1：允许")
	@NotNull(message = "Comments allowed cannot be empty")
	private Integer commentsAllowed;

	/**
	 * 游戏状态  0：下架   1：正常
	 */
	@ApiModelProperty("游戏状态  0：下架   1：正常")
	@NotNull(message = "Game status cannot be empty")
	private Integer status;

	/**
	 * 介绍
	 */
	@ApiModelProperty("介绍")
	@NotBlank(message = "Introduction cannot be empty")
	private String introduction;

}