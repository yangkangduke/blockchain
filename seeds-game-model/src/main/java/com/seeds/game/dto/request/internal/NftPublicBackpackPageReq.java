package com.seeds.game.dto.request.internal;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


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
public class NftPublicBackpackPageReq extends PageReq {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "nft物品名字")
    private String name;

    @ApiModelProperty(value = "是否分配。0未分配 1已分配", example = "0")
    private Integer isConfiguration;
}
