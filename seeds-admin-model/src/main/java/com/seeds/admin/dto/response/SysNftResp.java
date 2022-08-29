package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


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
	private BigDecimal price;

	@ApiModelProperty(value = "单位 USDC")
	private String unit;

	@ApiModelProperty(value = "是否在售  0：否   1：是")
	private Integer status;

	@ApiModelProperty(value = "状态  0：正常  1：创建中  2：创建失败  3：修改中  4：修改失败  5：删除中  6：删除失败")
	private Integer initStatus;

	@ApiModelProperty(value = "归属方类型  0：平台  1：uc用户")
	private Integer ownerType;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

	@ApiModelProperty(value = "归属人名称")
	private String ownerName;

}