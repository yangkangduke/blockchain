package com.seeds.admin.dto.mq;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * @author hang.yu
 * @date 2022/10/09
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "NftUpgradeMsgDTO", description = "NFT升级消息")
public class NftUpgradeMsgDTO extends NftMintMsgDTO {

    /**
     * 保留战绩NFT的id
     */
    @ApiModelProperty("保留战绩NFT的id")
    private Long nftId;

    /**
     * 消耗的NFT唯一标识集合
     */
    @ApiModelProperty("消耗NFT的id列表")
    private List<Long> nftIdList;

}
