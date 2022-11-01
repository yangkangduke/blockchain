package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2022/10/31
 */
@Data
public class SysCollectOrderHisReq extends PageReq {


    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "链 1：eth 3: tron")
    private Integer chain;

}
