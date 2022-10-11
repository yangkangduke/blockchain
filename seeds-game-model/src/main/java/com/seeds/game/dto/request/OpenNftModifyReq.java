package com.seeds.game.dto.request;

import com.seeds.admin.dto.request.SysNftModifyReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * NFT信息
 * 
 * @author hang.yu
 * @date 2022/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OpenNftModifyReq", description = "NFT信息")
public class OpenNftModifyReq extends SysNftModifyReq {

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