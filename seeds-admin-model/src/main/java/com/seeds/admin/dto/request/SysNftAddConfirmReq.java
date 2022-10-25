package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 系统NFT确认信息
 * 
 * @author hang.yu
 * @date 2022/10/24
 */
@Data
@ApiModel(value = "SysNftAddConfirmReq", description = "系统NFT确认信息")
public class SysNftAddConfirmReq {

	/**
	 * NFT的id
	 */
	@ApiModelProperty("NFT的id")
	@NotNull(message = "NFT id cannot be empty")
	private Long nftId;

	/**
	 * NFT上链的tokenId
	 */
	@ApiModelProperty("NFT上链的tokenId")
	@NotBlank(message = "NFT token id cannot be empty")
	private String newItemId;

	@ApiModelProperty("错误信息")
	private String message;

	/**
	 * 状态  0：正常  1：创建中  2：创建失败  5：删除中  6：删除失败
	 */
	@ApiModelProperty("状态  0：正常  1：创建中  2：创建失败  5：删除中  6：删除失败")
	@NotNull(message = "NFT init status cannot be empty")
	private Integer initStatus;

}