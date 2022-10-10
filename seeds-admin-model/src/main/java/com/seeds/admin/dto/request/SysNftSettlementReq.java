package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 系统NFT结算
 * 
 * @author hang.yu
 * @date 2022/10/10
 */
@Data
@ApiModel(value = "SysNftSettlementReq", description = "系统NFT结算")
public class SysNftSettlementReq {

	/**
	 * 对局锁定的NFT的id列表
	 */
	@ApiModelProperty("需要解锁的NFT列表")
	@NotEmpty(message = "NFT id list cannot be empty")
	private List<Long> nftIds;

	/**
	 * NFT结算数据列表
	 */
	@ApiModelProperty("NFT结算数据列表")
	@Valid
	private List<NftSettlement> settlementList;

	@Data
	@ApiModel(value = "NftSettlement", description = "NFT结算数据")
	public static class NftSettlement {

		@ApiModelProperty("需要变更的NFT唯一标识")
		@NotNull(message = "NFT id cannot be empty")
		private Long nftId;

		@ApiModelProperty("NFT新归属的用户id")
		@NotNull(message = "NFT new owner id cannot be empty")
		private Long newOwnerId;

		@ApiModelProperty("NFT新归属的用户名称")
		@NotBlank(message = "NFT new owner name cannot be empty")
		private String newOwnerName;

		@ApiModelProperty("NFT旧归属的用户id")
		@NotNull(message = "NFT old owner id cannot be empty")
		private Long oldOwnerId;

	}

}