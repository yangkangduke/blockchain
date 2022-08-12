package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


/**
 * 系统NFT信息
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "SysNftAddReq", description = "系统NFT信息")
public class SysNftAddReq {

	/**
	 * nft名称
	 */
	@ApiModelProperty("nft名称")
	@NotNull(message = "nft name cannot be empty")
	private String name;

	/**
	 * 游戏id
	 */
	@ApiModelProperty("游戏id")
	@NotNull(message = "Game id cannot be empty")
	private Long gameId;

	/**
	 * NFT类别id
	 */
	@ApiModelProperty("NFT类别id")
	@NotNull(message = "NFT type id cannot be empty")
	private Long nftTypeId;

	/**
	 * NFT图片链接
	 */
	@ApiModelProperty("NFT图片链接")
	@NotBlank(message = "NFT picture url cannot be empty")
	private String url;

	/**
	 * 图片文件id
	 */
	@ApiModelProperty("图片文件id")
	@NotNull(message = "NFT picture file id cannot be empty")
	private Long fileId;

	/**
	 * NFT归属人
	 */
	@ApiModelProperty("NFT归属人")
	@NotBlank(message = "NFT owner cannot be empty")
	private String owner;

	/**
	 * NFT价格
	 */
	@ApiModelProperty("NFT价格")
	@NotNull(message = "NFT price cannot be empty")
	private BigDecimal price;

	/**
	 * 单位 USDC
	 */
	@ApiModelProperty("NFT价格单位")
	@NotBlank(message = "NFT price unit cannot be empty")
	private String unit;

	/**
	 * NFT是否在售  0：否   1：是
	 */
	@ApiModelProperty("NFT是否在售  0：否   1：是")
	@NotNull(message = "NFT status cannot be empty")
	private Integer status;

	/**
	 * 合约地址
	 */
	@ApiModelProperty("合约地址")
	@NotBlank(message = "Contract address cannot be empty")
	private String contractAddress;

	/**
	 * 令牌id
	 */
	@ApiModelProperty("令牌id")
	@NotBlank(message = "Token id cannot be empty")
	private String tokenId;

	/**
	 * 令牌类型
	 */
	@ApiModelProperty("令牌类型")
	@NotBlank(message = "Token standard cannot be empty")
	private String tokenStandard;

	/**
	 * 区块链
	 */
	@ApiModelProperty("区块链")
	@NotBlank(message = "BlockChain cannot be empty")
	private String blockChain;

	/**
	 * 元数据
	 */
	@ApiModelProperty("元数据")
	@NotBlank(message = "Metadata cannot be empty")
	private String metadata;

	/**
	 * 创建人报酬比例
	 */
	@ApiModelProperty("创建人报酬比例")
	@NotNull(message = "Creator fees cannot be empty")
	private BigDecimal creatorFees;

	/**
	 * NFT属性列表
	 */
	@Valid
	@ApiModelProperty("NFT属性列表")
	private List<NftProperties> propertiesList;

}