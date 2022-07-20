package com.seeds.admin.dto.merchant.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统商家用户
 *
 * @author hang.yu
 * @date 2022/7/20
 */
@Data
@ApiModel(value = "系统商家用户信息")
public class SysMerchantUserResp {

    @ApiModelProperty(value = "用户名称")
    private String realName;

    @ApiModelProperty(value = "联系方式")
    private String mobile;

    @ApiModelProperty(value = "状态")
    private String status;

}
