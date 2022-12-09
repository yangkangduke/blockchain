package com.seeds.uc.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2022/12/8
 */

@Data
public class UserRegistrationResp {
    @ApiModelProperty(value = "总注册用户数")
    private Long totalRegisteredUsers;
    @ApiModelProperty(value = "今日新增注册用户数")
    private Long todayRegisteredUsers;
}
