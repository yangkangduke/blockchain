package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 签名
 * 
 * @author hang.yu
 * @date 2023/02/13
 */
@Data
@ApiModel(value = "OpenSignReq", description = "签名")
public class OpenSignReq {

	@ApiModelProperty(value = "访问键")
	@NotBlank(message = "Access key cannot be empty")
	private String accessKey;

	@ApiModelProperty(value = "签名")
	@NotBlank(message = "Signature key cannot be empty")
	private String signature;

	@ApiModelProperty(value = "时间戳")
	@NotNull(message = "Timestamp key cannot be empty")
	private Long timestamp;

}