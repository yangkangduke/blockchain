package com.seeds.game.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hewei
 * @since 2023-01-31
 */
@ApiModel(value = "NftPublicBackpackWebResp")
@Data
public class NftPublicBackpackWebResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("nft物品名称")
    private String name;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("等级")
    private Integer grade;

    @ApiModelProperty("耐久")
    private Integer durability;

    @ApiModelProperty("tokenId")
    private String tokenId;

    @ApiModelProperty("tokenAddress")
    private String tokenAddress;

    @ApiModelProperty("price")
    private BigDecimal price;

    @ApiModelProperty("状态 1 MINTED (mint成功)，2 LOCK (作为合成材料被临时锁定)，3 DEPOSITED (托管给平台) 4 UNDEPOSITED (解除托管)")
    private Integer state;

    @ApiModelProperty("基础属性")
    private String baseAttr;

    @ApiModelProperty("稀有属性")
    private String rarityAttr;

    @ApiModelProperty("区服名字")
    private String serverName;

    @ApiModelProperty("角色id")
    private Long serverRoleId;

    @ApiModelProperty("nft物品")
    private Long autoId;

    @ApiModelProperty("被动描述")
    private String passiveAttrDesc;

    @ApiModelProperty("特殊属性描述")
    private String specialAttrDesc;

    @ApiModelProperty("nftId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long eqNftId;
}
