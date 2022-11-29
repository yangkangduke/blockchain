package com.seeds.account.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 用户NFT历史价格
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NftPriceHisReq implements Serializable {

    @ApiModelProperty("NFT的id")
    @NotNull(message = "NFT id can not be empty")
    private Long nftId;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

}
