package com.seeds.admin.dto.sys.request;

import com.seeds.admin.dto.common.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@ApiModel(value = "用户列表请求入参")
public class SysUserPageReq extends PageReq {

    @ApiModelProperty(value = "姓名/手机号")
    private String nameOrMobile;

    @ApiModelProperty(value = "用户类型")
    private String userType;

}