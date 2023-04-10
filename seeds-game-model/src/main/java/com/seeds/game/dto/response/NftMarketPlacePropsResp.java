package com.seeds.game.dto.response;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 市场道具分页查询返回列表
 * @author dengyang
 * @since 2023-03-21
 */
@ApiModel(value = "NftMarketPlacePropsResp")
@Data
public class NftMarketPlacePropsResp implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("NFT id")
    private Long id;

    @ApiModelProperty("NFT编号")
    private String number;

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("NFT图片")
    private String image;

    @ApiModelProperty("tokenId")
    private String tokenId;

    @ApiModelProperty("NFT交易模式：1：Buy Now  2：On Auction")
    private Integer model;

    @ApiModelProperty("拍卖NFT(setting id)")
    private Long auctionId;

    @ApiModelProperty("NFT价格")
    private BigDecimal price;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("耐久度")
    private Integer durability;

    @ApiModelProperty("上架时间")
    private Long placeTime;

    @ApiModelProperty("健康属性值")
    private Integer health;

    @ApiModelProperty("装备属性值")
    private Integer armor;

    @ApiModelProperty("怪物属性值")
    private Integer vsMonster;

    @ApiModelProperty("属性综合介绍")
    private String passive;

    @ApiModelProperty("blade属性综合介绍")
    private String bladeBuff;

    @ApiModelProperty("稀有属性值")
    private String rarityAttrValue;

    @ApiModelProperty("基础属性值")
    private String baseAttrValue;
}
