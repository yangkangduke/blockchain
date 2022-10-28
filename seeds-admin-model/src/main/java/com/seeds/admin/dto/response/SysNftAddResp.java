package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统NFT添加
 * 
 * @author hang.yu
 * @date 2022/10/24
 */
@Data
@ApiModel(value = "SysNftAddResp", description = "系统NFT添加信息")
public class SysNftAddResp {

	@ApiModelProperty(value = "NFT的id")
	private Long nftId;

	@ApiModelProperty(value = "上链加密hash")
	private String metadataHash;

}