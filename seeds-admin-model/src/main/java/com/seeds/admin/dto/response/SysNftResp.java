package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统NFT
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "SysNftResp", description = "系统NFT信息")
public class SysNftResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "编号")
	private String number;

	@ApiModelProperty(value = "图片")
	private String picture;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "游戏名称")
	private String gameName;

	@ApiModelProperty(value = "类型")
	private String typeCode;

	@ApiModelProperty(value = "类型名称")
	private String typeName;

	@ApiModelProperty(value = "价格")
	private String price;

	@ApiModelProperty(value = "是否在售  0：否   1：是")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

	@ApiModelProperty(value = "归属人名称")
	private String ownerName;

}