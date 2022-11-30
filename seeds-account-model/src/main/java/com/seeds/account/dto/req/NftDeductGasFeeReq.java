package com.seeds.account.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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

    @ApiModelProperty(value = "手续费")
    private String gasFees;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty("用户id")
    private Long userId;

}
