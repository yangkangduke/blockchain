package com.seeds.admin.dto.request;

import com.seeds.common.enums.TargetSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(value = "NftPropertiesValueReq", description = "NFT属性")
public class NftOwnerChangeReq {

    /**
     * NFT的id
     */
    @ApiModelProperty("NFT的id")
    @NotNull(message = "NFT id cannot be empty")
    private Long id;

    @NotNull
    @ApiModelProperty("NFT 归属方类型  0：平台  1：uc用户")
    private Integer ownerType;
    /**
     * NFT的归属人id
     */
    @ApiModelProperty("NFT的归属人id")
    @NotNull(message = "NFT owner id cannot be empty")
    private Long ownerId;

    @ApiModelProperty("记录id")
    @NotNull(message = "NFT actionHistory id cannot be empty")
    private Long actionHistoryId;

    @ApiModelProperty("offer的id")
    private Long offerId;

    @ApiModelProperty("请求来源")
    private TargetSource source;

    @ApiModelProperty("售卖金额")
    @NotNull(message = "NFT amount cannot be empty")
    private BigDecimal amount;

    @ApiModelProperty("币种")
    private String currency;

}
