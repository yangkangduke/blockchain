package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统NFT属性类别
 * 
 * @author hang.yu
 * @date 2022/8/19
 */
@Data
@ApiModel(value = "SysNftPropertiesTypeBriefResp", description = "系统NFT属性类别")
public class SysNftPropertiesTypeBriefResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "编码")
	private String code;

	@ApiModelProperty(value = "名称")
	private String name;

}