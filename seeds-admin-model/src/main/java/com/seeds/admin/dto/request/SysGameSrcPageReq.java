package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2022/12/21
 */

@Data
@ApiModel(value = "sysGameSrcPageReq", description = "游戏资源分页请求入参")
public class SysGameSrcPageReq extends PageReq {
    @ApiModelProperty(value = "资源类型 1 主页视频；2 安装包；3 补丁包")
    private Integer srcType;
    @ApiModelProperty(value = "文件名")
    private String fileName;
    @ApiModelProperty(value = "状态  1：启动   0：禁用")
    private Integer status;
}
