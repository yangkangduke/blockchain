package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统NFT升级
 * 
 * @author hang.yu
 * @date 2022/10/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysNftUpgradeReq", description = "系统NFT升级")
public class SysNftUpgradeReq extends SysNftAddReq {

	/**
	 * 操作用户id
	 */
	private Long userId;

	/**
	 * 保留战绩NFT的id
	 */
	@ApiModelProperty("保留战绩NFT的id")
	@NotNull(message = "NFT id cannot be empty")
	private Long nftId;

	/**
	 * 消耗的NFT唯一标识集合
	 */
	@ApiModelProperty("消耗NFT的id列表")
	@NotEmpty(message = "NFT id list cannot be empty")
	private List<Long> nftIdList;

}