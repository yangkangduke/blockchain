package com.seeds.notification.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: hewei
 * @date 2022/9/21
 */
@Data
@ApiModel(value = "保存通知请求入参")
public class NoticeSaveReq {
    @ApiModelProperty("用户id")
    private Long ucUserId;

    @ApiModelProperty("消息内容")
    private String content;
}
