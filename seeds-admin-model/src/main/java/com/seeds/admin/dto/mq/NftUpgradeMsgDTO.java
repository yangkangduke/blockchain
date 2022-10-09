package com.seeds.admin.dto.mq;

import com.seeds.admin.dto.request.NftPropertiesReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hang.yu
 * @date 2022/10/09
 */

@Data
@ApiModel(value = "NftUpgradeMsgDTO", description = "NFT升级消息")
public class NftUpgradeMsgDTO {

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

}
