package com.seeds.game.dto.response;

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
@ApiModel(value = "NftEventEquipmentResp")
@Data
public class NftEventEquipmentResp implements Serializable {

    @ApiModelProperty("imageUrl")
    private String imageUrl;

    @ApiModelProperty("是否nft 1是 0 否")
    private Integer isNft;

    @ApiModelProperty("是否被消耗 1是 0 否")
    private Integer isConsume;

    @ApiModelProperty("json文件地址")
    private String jsonUrl;

    @ApiModelProperty("属性")
    private String attributes;

    @ApiModelProperty("装备等级")
    private Integer lvl;

    @ApiModelProperty("稀有属性值")
    private String rarityAttrValue;

    @ApiModelProperty("基础属性值")
    private String baseAttrValue;
}
