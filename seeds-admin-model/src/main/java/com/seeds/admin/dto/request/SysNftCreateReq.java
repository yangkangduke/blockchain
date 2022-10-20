package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 系统NFT创建信息
 * 
 * @author hang.yu
 * @date 2022/10/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysNftCreateReq", description = "系统NFT创建信息")
public class SysNftCreateReq extends SysNftAddReq {

	/**
	 * 归属人id
	 */
	private Long ownerId;

	/**
	 * 归属人名称
	 */
	private String ownerName;

	/**
	 * 归属人类型
	 */
	private Integer ownerType;

	@ApiModelProperty("NFT模板编号")
	@NotBlank(message = "NFT template number cannot be empty")
	private String nftNo;

	@ApiModelProperty("NFT的gas费")
	@NotBlank(message = "NFT gas fees cannot be empty")
	private String gasFees;

}