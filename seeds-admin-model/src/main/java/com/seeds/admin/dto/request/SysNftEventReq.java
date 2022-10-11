package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 系统NFT事件信息
 *
 * @author hang.yu
 * @date 2022/10/08
 */
@Data
@ApiModel(value = "SysNftEventReq", description = "系统NFT事件信息")
public class SysNftEventReq {

	/**
	 * 事件的id
	 */
	@ApiModelProperty("事件的id")
	@NotNull(message = "NFT event id cannot be empty")
	private Long eventId;

	/**
	 * 触发时间
	 */
	@ApiModelProperty("触发时间")
	@NotNull(message = "NFT event trigger time cannot be empty")
	private Long triggerTime;

}