package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统用户简略信息
 * 
 * @author hang.yu
 * @date 2022/7/20
 */
@Data
@ApiModel(value = "SysUserBriefResp", description = "系统用户简略信息")
public class SysUserBriefResp {

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	private Long id;

	/**
	 * 姓名
	 */
	@ApiModelProperty("姓名")
	private String realName;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String mobile;

}