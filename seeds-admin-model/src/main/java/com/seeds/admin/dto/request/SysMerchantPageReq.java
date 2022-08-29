package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/19
 */
@Data
@ApiModel(value = "SysMerchantPageReq", description = "用户商家请求入参")
public class SysMerchantPageReq extends PageReq {

    @ApiModelProperty(value = "名称/联系方式")
    private String nameOrMobile;

}
