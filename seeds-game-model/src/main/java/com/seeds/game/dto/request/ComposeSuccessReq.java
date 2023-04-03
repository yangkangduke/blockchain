package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/30
 */
@Data
@ApiModel(value = "NftMintSuccessReq")
public class ComposeSuccessReq {

    @ApiModelProperty(value = "nftEventId")
    private Long eventId;

    @ApiModelProperty(value = "是否自动托管 1 是 0 否")
    private Integer autoDeposite;

    @ApiModelProperty(value = "销毁装备地址")
    private String mintAddresses;

    @ApiModelProperty(value = "销毁装备的签名数组")
    private String sigs;

    @ApiModelProperty(value = "用户钱包地址")
    private String walletAddress;
}
