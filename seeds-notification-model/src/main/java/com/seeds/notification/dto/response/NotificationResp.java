package com.seeds.notification.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: hewei
 * @date 2022/9/20
 */

@ApiModel(description = "系统通知")
@Data
public class NotificationResp implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long ucUserId;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("是否已读，0 未读 1 已读")
    private Integer hasRead;
}
