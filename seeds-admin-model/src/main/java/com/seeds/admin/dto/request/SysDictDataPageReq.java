package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yang.deng
 * @date 2022/8/15
 */

@Data
@ApiModel(value = "SysDictDataPageReq", description = "字典数据分页请求入参")
public class SysDictDataPageReq extends PageReq{

    @ApiModelProperty(value = "名称")
    private String dictLabel;
}
