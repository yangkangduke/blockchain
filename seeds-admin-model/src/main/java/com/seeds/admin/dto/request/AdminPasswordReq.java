package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "后台用户密码")
public class AdminPasswordReq {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "原密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    private String newPassword;

}
