package com.seeds.admin.dto.request;

import com.seeds.admin.dto.request.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "游戏分页请求入参")
public class SysGamePageReq extends PageReq {

    @ApiModelProperty(value = "名称")
    private String name;

}
