package com.seeds.game.dto.request;

import com.seeds.common.dto.PageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * NFT公共背包
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "OpenNftPublicBackpackPageReq")
public class OpenNftPublicBackpackPageReq extends NftPublicBackpackPageReq implements Serializable {

    @ApiModelProperty(value = "访问键", required = true)
    @NotBlank(message = "Access key cannot be empty")
    private String accessKey;

    @ApiModelProperty(value = "签名", required = true)
    @NotBlank(message = "Signature key cannot be empty")
    private String signature;

    @ApiModelProperty(value = "时间戳", required = true)
    @NotNull(message = "Timestamp key cannot be empty")
    private Long timestamp;
}
