package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "SysNftTypeReq", description = "NFT类别请求入参")
public class SysNftTypeReq {

    @ApiModelProperty(value = "名称")
    private String name;

}
