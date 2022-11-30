package com.seeds.uc.dto.request;

import com.seeds.common.enums.CurrencyEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * NFT正向拍卖入参
 * </p>
 *
 * @author hang.yu
 * @since 2022-10-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NFTForwardAuctionReq implements Serializable {

    @ApiModelProperty("admin中的NFT的id")
    @NotNull
    private Long nftId;

    @ApiModelProperty("拍卖的初始最低价格")
    @NotNull
    private BigDecimal price;

    @ApiModelProperty("一口价")
    @NotNull
    private BigDecimal maxPrice;

    @ApiModelProperty("价格单位")
    @NotNull
    private CurrencyEnum currency;

    @ApiModelProperty("结算时间")
    private Long settlementTime;

    @ApiModelProperty("拥有NFT的用户id")
    private Long userId;

}
