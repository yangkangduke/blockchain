package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * NFT 价格
 *
 * @author: hewei
 * @date 2023/1/31
 */
@Data
public class OpenGetNFTPriceReq extends OpenSignReq {

    @ApiModelProperty(value = "autoIds", required = true)
    @NotNull(message = "autoIds cannot be empty")
    private List<Long> autoIds;

}
