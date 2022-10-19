package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 系统NFT手续费
 * 
 * @author hang.yu
 * @date 2022/10/19
 */
@Data
@ApiModel(value = "SysNftGasFeesResp", description = "系统NFT手续费")
public class SysNftGasFeesResp {

	@ApiModelProperty(value = "价格")
	private BigDecimal price;

	@ApiModelProperty(value = "单位")
	private String unit;

}