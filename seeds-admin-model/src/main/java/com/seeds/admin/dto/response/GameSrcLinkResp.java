package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2022/12/17
 */
@ApiModel(value = "GameSrcLinkResp", description = "游戏资源Link信息")
@Data
public class GameSrcLinkResp {

    @ApiModelProperty("资源名称")
    private String srcName;

    @ApiModelProperty("适用系统 0 不限 1 Windows 2 mac")
    private Integer os;

    @ApiModelProperty("资源地址")
    private String url;

}
