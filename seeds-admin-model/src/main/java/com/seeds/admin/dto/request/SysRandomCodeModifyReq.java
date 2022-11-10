package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 随机码编辑信息
 * 
 * @author hang.yu
 * @date 2022/11/08
 */
@Data
@ApiModel(value = "SysRandomCodeModifyReq", description = "随机码编辑信息")
public class SysRandomCodeModifyReq {

	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	@NotBlank(message = "Batch number can not be empty!")
	private String batchNo;

	/**
	 * 描述
	 */
	@ApiModelProperty("描述")
	private String desc;

	/**
	 * 过期时间
	 */
	@ApiModelProperty("过期时间")
	private Long expireTime;

}