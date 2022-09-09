package com.seeds.admin.dto.response;

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
@ApiModel(value = "SysMerchantUserResp", description = "系统商家用户信息")
public class SysMerchantUserResp {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String realName;

    @ApiModelProperty(value = "联系方式")
    private String mobile;

    @ApiModelProperty(value = "状态")
    private Integer status;

}
