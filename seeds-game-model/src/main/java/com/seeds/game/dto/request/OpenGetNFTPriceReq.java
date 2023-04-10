package com.seeds.game.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * NFT 价格
 *
 * @author: hewei
 * @date 2023/1/31
 */
@Data
public class OpenGetNFTPriceReq extends OpenSignReq {

    @ApiModelProperty(value = "autoIds", required = true)
    @NotBlank(message = "autoIds cannot be empty")
    private String autoIds;

}
