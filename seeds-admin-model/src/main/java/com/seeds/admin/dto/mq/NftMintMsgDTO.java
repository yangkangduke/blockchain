package com.seeds.admin.dto.mq;

import com.seeds.admin.dto.request.NftPropertiesReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: hewei
 * @date 2022/9/13
 */

@Data
@ApiModel(value = "NftMintMsgDTO", description = "NftMintMsgDTO")
public class NftMintMsgDTO {

    @ApiModelProperty("NFTId")
    private Long id;

    @ApiModelProperty("NFT名称")
    private String name;

    @ApiModelProperty("游戏id")
    private Long gameId;

    @ApiModelProperty("NFT类别id")
    private Long nftTypeId;

    @ApiModelProperty("NFT描述")
    private String description;

    @ApiModelProperty("NFT价格")
    private BigDecimal price;

    @ApiModelProperty("NFT价格单位")
    private String unit;

    @ApiModelProperty("状态  0：停售   1：在售")
    private Integer status;

    @ApiModelProperty("NFT属性列表")
    private List<NftPropertiesReq> propertiesList;

    @ApiModelProperty("NFT图片")
    private String imageFileHash;

    @ApiModelProperty("回调通知链接")
    private String callbackUrl;

    @ApiModelProperty("游戏方用户id")
    private Long accId;

    /**
     * 归属人id
     */
    private Long ownerId;

}
