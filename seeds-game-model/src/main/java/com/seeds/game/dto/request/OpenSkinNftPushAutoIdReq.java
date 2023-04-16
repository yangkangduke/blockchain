package com.seeds.game.dto.request;

import com.seeds.admin.dto.SkinNftPushAutoIdReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 皮肤nft autoId推送
 *
 * @author hewei
 * @date 2023/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OpenSkinNftPushAutoIdReq")
public class OpenSkinNftPushAutoIdReq extends SkinNftPushAutoIdReq {
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
