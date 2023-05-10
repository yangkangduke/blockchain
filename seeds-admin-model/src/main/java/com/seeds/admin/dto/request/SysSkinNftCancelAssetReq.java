package com.seeds.admin.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hewei
 * @date 2023/4/26
 */
@Data
@ApiModel(value = "SysSkinNftCancelAssetReq")
public class SysSkinNftCancelAssetReq {
    @ApiModelProperty("id")
    private Long id;

//    @ApiModelProperty("交易hash")
//    private String sig;
//
//    @ApiModelProperty("上架收据")
//    private String listReceipt;

}
