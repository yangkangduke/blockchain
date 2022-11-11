package com.seeds.uc.dto.request;

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
public class NFTBuyReq implements Serializable {

    @ApiModelProperty("admin中的NFT的id")
    @NotNull
    private Long nftId;

    @ApiModelProperty("请求来源")
    private TargetSource source = TargetSource.UC;

    @ApiModelProperty("购买用户id")
    private Long userId;

}
