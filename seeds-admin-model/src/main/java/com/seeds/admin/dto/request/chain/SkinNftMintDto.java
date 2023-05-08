package com.seeds.admin.dto.request.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/4/26
 */
@Data
public class SkinNftMintDto {

    @ApiModelProperty("铸造数量")
    private Integer amount;
    private String group = "SKIN";
}
