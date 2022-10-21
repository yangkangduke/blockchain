package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@ApiModel(value = "SysNftGasFeesReq", description = "系统NFT费用")
public class SysNftGasFeesReq {

    @ApiModelProperty(value = "NFT模板编号")
    private String nftNo;

}
