package com.seeds.game.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author hang.yu
 * @date 2023/05/27
 */

@Data
@ApiModel(value = "NftMyOfferResp")
public class NftMyOfferResp {

    @ApiModelProperty("拍卖出价Id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long bidingId;

    @ApiModelProperty("NFt的Id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long nftId;

    @ApiModelProperty("出价时间")
    private Long offerTime;

    @ApiModelProperty("出价价格")
    private BigDecimal price;

    @ApiModelProperty("价格差异")
    private String difference;

    @ApiModelProperty("NFT图片")
    private String nftImage;

    @ApiModelProperty("NFT编号")
    private String nftNo;

    @ApiModelProperty("收据")
    private String receipt;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("是否可以取消 0 不能 1 能")
    private Integer cancelFlag = 1;

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("稀有属性值")
    private String rarityAttrValue;

    @ApiModelProperty("1 装备 2 道具 3 英雄")
    private Integer itemType;

}
