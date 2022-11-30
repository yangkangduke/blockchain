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
 * NFT出价入参
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NftMakeOfferReq implements Serializable {

    @ApiModelProperty("admin中的NFT的id")
    @NotNull
    private Long nftId;

    @ApiModelProperty("价格")
    @NotNull
    private BigDecimal price;

    @ApiModelProperty("单位")
    @NotNull
    private CurrencyEnum currency;

    @ApiModelProperty("过期时间")
    private Long expireTime;

    @ApiModelProperty("出价用户id")
    private Long userId;

}
