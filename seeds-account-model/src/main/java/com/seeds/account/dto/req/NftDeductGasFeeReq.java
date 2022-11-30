package com.seeds.account.dto.req;

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
 * NFT手续费扣除
 * </p>
 *
 * @author hang.yu
 * @since 2022-11-02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NftDeductGasFeeReq implements Serializable {

    @ApiModelProperty(value = "价格")
    @NotNull
    private BigDecimal price;

    @ApiModelProperty("用户id")
    private Long userId;

}
