package com.seeds.game.dto.request;

import com.seeds.admin.dto.request.SysNftSettlementReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 系统NFT结算
 * 
 * @author hang.yu
 * @date 2022/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysNftSettlementReq", description = "系统NFT结算")
public class OpenNftSettlementReq extends SysNftSettlementReq {

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