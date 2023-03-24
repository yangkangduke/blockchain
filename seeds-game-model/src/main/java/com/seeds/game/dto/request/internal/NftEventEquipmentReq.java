package com.seeds.game.dto.request.internal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * nft Event 关联的装备
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */
@ApiModel(value = "NftEventEquipmentReq")
@Data
public class NftEventEquipmentReq implements Serializable {

    @ApiModelProperty("imageUrl")
    @NotBlank
    private String imageUrl;

    @ApiModelProperty("游戏方autoId")
    @NotNull
    private Long autoId;

    @ApiModelProperty("游戏方confId")
    @NotNull
    private Long configId;

    @ApiModelProperty("是否nft 1是 0 否")
    @NotNull
    private Integer isNft;

    @ApiModelProperty("是否被消耗 1是 0 否，合成时作为合成材料的装备被消耗（传1），mint和合成的目的装备（传0）")
    @NotNull
    private Integer isConsume;

    @ApiModelProperty("属性 传json")
    @NotBlank
    private String attributes;
}
