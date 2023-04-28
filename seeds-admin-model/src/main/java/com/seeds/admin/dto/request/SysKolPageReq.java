package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * KOL信息
 * @author hang.yu
 * @date 2023/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysKolPageReq", description = "KOL分页请求入参")
public class SysKolPageReq extends PageReq {

    @ApiModelProperty(value = "邮箱")
    private String email;

}
