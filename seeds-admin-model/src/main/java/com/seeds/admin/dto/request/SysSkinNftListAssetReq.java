package com.seeds.admin.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author hewei
 * @date 2023/4/26
 */
@Data
@ApiModel(value = "SysSkinNftListAssetReq")
public class SysSkinNftListAssetReq {

    @ApiModelProperty("勾选条目的id集合")
    private List<Long> ids;

    @ApiModelProperty("固定价格")
    private BigDecimal price;

    @ApiModelProperty("auctionHouseAddress")
    private String auctionHouseAddress;

    @ApiModelProperty("uuid")
    @NotBlank
    private String uuid;
}
