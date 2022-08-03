package com.seeds.admin.dto.response;

import com.seeds.admin.dto.request.NftProperties;
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

	@ApiModelProperty(value = "游戏名称")
	private String gameName;

	@ApiModelProperty(value = "类型")
	private String typeName;

	@ApiModelProperty(value = "价格")
	private String price;

	@ApiModelProperty(value = "是否在售  0：否   1：是")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

	@ApiModelProperty(value = "归属人")
	private String owner;

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

	@ApiModelProperty("NFT属性列表")
	private List<NftProperties> propertiesList;

}