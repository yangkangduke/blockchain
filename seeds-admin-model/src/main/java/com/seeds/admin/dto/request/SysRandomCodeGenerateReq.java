package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 随机码生成信息
 * 
 * @author hang.yu
 * @date 2022/11/08
 */
@Data
@ApiModel(value = "SysRandomCodeGenerateReq", description = "随机码生成信息")
public class SysRandomCodeGenerateReq {

	/**
	 * 类型 1：邀请码
	 */
	@ApiModelProperty("类型 1：邀请码")
	@NotNull(message = "Random code type cannot be empty")
	private Integer type;

	/**
	 * 长度
	 */
	@ApiModelProperty("长度")
	@NotNull(message = "Random code length cannot be empty")
	private Integer length;

	/**
	 * 数量
	 */
	@ApiModelProperty("数量")
	@NotNull(message = "Random code number cannot be empty")
	private Integer number;

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