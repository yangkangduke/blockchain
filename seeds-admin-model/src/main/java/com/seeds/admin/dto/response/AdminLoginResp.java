package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@Builder
@ApiModel(value = "AdminLoginResp", description = "系统用户登录")
public class AdminLoginResp {

    /**
     * 认证令牌
     */
    @ApiModelProperty("认证令牌")
    private String token;

    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")
    private Integer expire;

}