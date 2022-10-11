package com.seeds.game.dto.request;

import com.seeds.admin.dto.request.SysNftHonorModifyReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * NFT战绩记录信息
 *
 * @author hang.yu
 * @date 2022/10/10
 */
@Data
@ApiModel(value = "OpenNftHonorModifyReq", description = "NFT战绩记录信息")
public class OpenNftHonorModifyReq {

    @ApiModelProperty(value = "访问键")
    @NotBlank(message = "Access key cannot be empty")
    private String accessKey;

    @ApiModelProperty(value = "签名")
    @NotBlank(message = "Signature key cannot be empty")
    private String signature;

    @ApiModelProperty(value = "时间戳")
    @NotNull(message = "Timestamp key cannot be empty")
    private Long timestamp;

    @ApiModelProperty("NFT的战绩记录")
    @NotEmpty(message = "NFT honor list cannot be empty")
    @Valid
    private List<SysNftHonorModifyReq> honorList;

}
