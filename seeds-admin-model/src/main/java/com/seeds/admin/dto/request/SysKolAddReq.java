package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * KOL信息
 * @author hang.yu
 * @date 2023/4/26
 */
@Data
@ApiModel(value = "SysKolAddReq", description = "KOL信息")
public class SysKolAddReq {

	@ApiModelProperty(value = "KOL名称")
	@NotBlank(message = "Name cannot be empty")
	private String name;

	@ApiModelProperty(value = "邮箱")
	@NotBlank(message = "Email cannot be empty")
	private String email;

	@ApiModelProperty(value = "备注")
	private String memo;

}