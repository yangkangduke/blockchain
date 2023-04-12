package com.seeds.game.dto.response;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

    @ApiModelProperty("nft id")
    private Long nftId;

    @ApiModelProperty("NFT编号")
    private String number;

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("NFT图片")
    private String image;

    @ApiModelProperty("tokenId")
    private String tokenId;

    @ApiModelProperty("NFT状态：0：UnDeposited  1：Deposited 2:On shelf 3:On auction 4:In settlement 5: Burned")
    private Integer state;

    @ApiModelProperty("拍卖NFT(setting id)")
    private Long auctionId;

    @ApiModelProperty("NFT价格")
    private BigDecimal price;

    @ApiModelProperty("NFT最近历史交易价格")
    private BigDecimal lastSale;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("当前耐久度")
    private Integer durability;

    @ApiModelProperty("最大耐久度")
    private Integer maxDurability;

    @ApiModelProperty("上架时间")
    private Long placeTime;

    @ApiModelProperty("稀有属性值")
    private String rarityAttrValue;

    @ApiModelProperty("基础属性值")
    private String baseAttrValue;
}
