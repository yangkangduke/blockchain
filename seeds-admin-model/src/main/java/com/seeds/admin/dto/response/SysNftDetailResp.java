package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * 系统NFT详情
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "SysNftDetailResp", description = "系统NFT详情")
public class SysNftDetailResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "编号")
	private String number;

	@ApiModelProperty(value = "图片")
	private String picture;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "游戏id")
	private Long gameId;

	@ApiModelProperty(value = "游戏名称")
	private String gameName;

	@ApiModelProperty(value = "类型")
	private String typeName;

	@ApiModelProperty(value = "价格")
	private BigDecimal price;

	@ApiModelProperty(value = "单位")
	private String unit;

	@ApiModelProperty(value = "描述")
	private String description;

	@ApiModelProperty(value = "是否在售  0：否   1：是")
	private Integer status;

	@ApiModelProperty(value = "状态  0：正常  1：创建中  2：创建失败  3：修改中  4：修改失败  5：删除中  6：删除失败 ")
	private Integer initStatus;

	@ApiModelProperty(value = "归属方类型  0：平台  1：uc用户")
	private Integer ownerType;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

	@ApiModelProperty(value = "归属人ID")
	private Long ownerId;

	@ApiModelProperty(value = "归属人名称")
	private String ownerName;

	@ApiModelProperty("合约地址")
	private String contractAddress;

	@ApiModelProperty("令牌id")
	private String tokenId;

	@ApiModelProperty("令牌类型")
	private String tokenStandard;

	@ApiModelProperty("区块链")
	private String blockChain;

	@ApiModelProperty("元数据")
	private String metadata;

	@ApiModelProperty("创建人报酬比例")
	private BigDecimal creatorFees;

	@ApiModelProperty("收藏量")
	private Long collections;

	@ApiModelProperty("浏览量")
	private Long views;

	@ApiModelProperty(value = "锁定标记，已锁定不可操作  0：未锁定 1：已锁定")
	private Integer lockFlag;

	@ApiModelProperty("NFT属性列表")
	private List<NftPropertiesResp> propertiesList;

}