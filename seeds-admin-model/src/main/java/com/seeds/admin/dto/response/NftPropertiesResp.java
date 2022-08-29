package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统NFT属性
 * 
 * @author hang.yu
 * @date 2022/8/13
 */
@Data
@ApiModel(value = "NftPropertiesResp", description = "系统NFT属性")
public class NftPropertiesResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "类别id")
	private Long typeId;

	@ApiModelProperty(value = "编码")
	private String code;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "属性值")
	private String value;

}