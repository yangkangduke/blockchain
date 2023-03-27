package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * nft Event 关联的装备
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */
@TableName("ga_nft_event_equipment")
@ApiModel(value = "NftEventEquipment对象", description = "nft Event 关联的装备")
@Data
public class NftEventEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("event id")
    private Long eventId;

    @ApiModelProperty("imageUrl")
    private String imageUrl;

    @ApiModelProperty("游戏方autoId")
    private Long autoId;

    @ApiModelProperty("游戏方confId")
    private Long configId;

    @ApiModelProperty("是否nft")
    private Integer isNft;

    @ApiModelProperty("tokenId")
    private String tokenId;

    @ApiModelProperty("是否被消耗")
    private Integer isConsume;

    @ApiModelProperty("json文件地址")
    private String jsonUrl;

    @ApiModelProperty("属性")
    private String attributes;

    @ApiModelProperty("nft元数据")
    private String metadata;
}
