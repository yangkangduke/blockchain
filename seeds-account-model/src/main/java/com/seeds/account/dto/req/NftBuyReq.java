package com.seeds.account.dto.req;

import com.seeds.common.enums.TargetSource;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 用户账号交易
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NftBuyReq implements Serializable {

    @ApiModelProperty("NFT的id")
    @NotNull(message = "NFT id can not be empty")
    private Long nftId;

    @ApiModelProperty("购买用户端")
    private TargetSource source = TargetSource.UC;

    @ApiModelProperty("购买用户id")
    private Long userId;

}
