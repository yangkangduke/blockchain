package com.seeds.game.dto.request.internal;

import com.seeds.common.dto.PageReq;
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
@ApiModel(value = "NftPublicBackpackPageReq")
public class NftPublicBackpackPageReq extends PageReq implements Serializable {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty("是否分配。0未分配 1已分配")
    private Integer isConfiguration;
}
