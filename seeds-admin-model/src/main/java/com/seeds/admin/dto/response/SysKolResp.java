package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * KOL信息
 * @author hang.yu
 * @date 2023/4/26
 */
@Data
@ApiModel(value = "SysKolResp", description = "KOL信息")
public class SysKolResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "KOL名称")
	private String name;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "备注")
	private String memo;

	@ApiModelProperty(value = "邀请链接")
	private String inviteUrl;

	@ApiModelProperty(value = "状态  0：禁用   1：启用")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

}