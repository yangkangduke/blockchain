package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 随机码明细
 * 
 * @author hang.yu
 * @date 2022/11/08
 */
@Data
@ApiModel(value = "SysRandomCodeDetailResp", description = "随机码明细")
public class SysRandomCodeDetailResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "批次号")
	private String batchNo;

	@ApiModelProperty(value = "随机码")
	private String code;

	@ApiModelProperty(value = "使用标记 0：未使用 1：已使用")
	private Integer useFlag;

}