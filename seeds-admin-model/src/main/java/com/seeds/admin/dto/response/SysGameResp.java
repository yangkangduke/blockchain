package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 系统游戏
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "SysGameResp", description = "系统游戏信息")
public class SysGameResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "图片地址")
	private String pictureUrl;

	@ApiModelProperty(value = "图片文件id")
	private Long pictureFileId;

	@ApiModelProperty(value = "视频地址")
	private String videoUrl;

	@ApiModelProperty(value = "视频文件id")
	private Long videoFileId;

	@ApiModelProperty(value = "游戏名称")
	private String name;

	@ApiModelProperty(value = "简介")
	private String brief;

	@ApiModelProperty(value = "价格")
	private BigDecimal price;

	@ApiModelProperty(value = "评分")
	private BigDecimal rank;

	@ApiModelProperty("收藏量")
	private Long collections;

	@ApiModelProperty(value = "单位")
	private String unit;

	@ApiModelProperty(value = "官方网址")
	private String officialUrl;

	@ApiModelProperty("下载地址")
	private String downloadUrl;

	@ApiModelProperty("社区地址")
	private String communityUrl;

	@ApiModelProperty("开发者")
	private String developer;

	@ApiModelProperty("是否允许评论  0：不允许   1：允许")
	private Integer commentsAllowed;

	@ApiModelProperty("游戏状态  0：下架   1：正常")
	private Integer status;

	@ApiModelProperty("介绍")
	private String introduction;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

}