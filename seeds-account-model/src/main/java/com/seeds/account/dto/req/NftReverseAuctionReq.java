package com.seeds.account.dto.req;

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
 * NFT反向拍卖入参
 * </p>
 *
 * @author hang.yu
 * @since 2022-10-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NftReverseAuctionReq implements Serializable {

    @ApiModelProperty("admin中的NFT的id")
    @NotNull
    private Long nftId;

    @ApiModelProperty("拍卖的初始最高价格")
    @NotNull
    private BigDecimal price;

    @ApiModelProperty("价格单位")
    @NotNull
    private CurrencyEnum currency;

    @ApiModelProperty("间隔时间")
    private Long intervalTime;

    @ApiModelProperty("间隔时间单位 m,h,d,s")
    private String intervalUnit;

    @ApiModelProperty("下降百分比")
    private BigDecimal dropPoint;

    @ApiModelProperty("拥有NFT的用户id")
    private Long userId;

}
