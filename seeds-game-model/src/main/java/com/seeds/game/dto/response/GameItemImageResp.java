package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * 游戏资源图片
 * 
 * @author hang.yu
 * @date 2023/5/18
 */
@Data
@ApiModel(value = "GameItemImageResp", description = "游戏资源图片")
public class GameItemImageResp {

	@ApiModelProperty("id")
	private Long id;

	@ApiModelProperty("道具分类名称")
	private String name;

	@ApiModelProperty("图片地址")
	private String imgUrl;

	@ApiModelProperty("分类id")
	private Long itemId;

	@ApiModelProperty("类别 1装备 2道具 3英雄")
	private Integer itemType;

	@ApiModelProperty("职业名称")
	private String careerName;

	@ApiModelProperty("类别 作为父节点使用")
	private String type;

	@ApiModelProperty("子节点列表")
	private List<GameItemImageResp> children = new ArrayList<>();

}