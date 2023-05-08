package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/30
 */
@Data
@ApiModel(value = "NftMintEquipReq")
public class NftMintEquipReq {

    @ApiModelProperty(value = "nftEventId")
    private Long eventId;

    @ApiModelProperty(value = "转账成功后的签名信息：校验充值是否正确")
    private String feeHash;

    @ApiModelProperty(value = "用户钱包地址：当不需要托管的时候，新mint出来的token需要转移给他")
    private String toUserAddress;

    @ApiModelProperty(value = "是否自动托管 1 是 0 否")
    private Integer autoDeposite;
}
