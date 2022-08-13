package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/8/13
 */
@Data
@ApiModel(value = "SysNftPropertiesTypePageReq", description = "NFT属性类别分页请求入参")
public class SysNftPropertiesTypePageReq extends PageReq {

    @ApiModelProperty(value = "NFT名称")
    private String name;

}
