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

    @ApiModelProperty("NFT编号")
    private String number;

    @ApiModelProperty("NFT图片")
    private String image;

    @ApiModelProperty("tokenId")
    private String tokenId;

    @ApiModelProperty("NFT交易模式：0：Buy Now  1：On Auction")
    private Integer model;

    @ApiModelProperty("NFT价格")
    private BigDecimal price;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("耐久度")
    private Integer durability;

}
