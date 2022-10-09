package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 系统NFT锁定
 * 
 * @author hang.yu
 * @date 2022/10/9
 */
@Data
@ApiModel(value = "SysNftLockReq", description = "系统NFT锁定")
public class SysNftLockReq {

	/**
	 * NFT的id
	 */
	@ApiModelProperty("NFT的id")
	@NotNull(message = "NFT id cannot be empty")
	private Long nftId;

	/**
	 * NFT消耗的耐久值
	 */
	@ApiModelProperty("消耗的耐久值")
	@NotNull(message = "NFT endurance cannot be empty")
	private Integer endurance;

}