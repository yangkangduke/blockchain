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

    @ApiModelProperty("通知类型")
    private String notificationType;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("是否已读，0 未读 1 已读")
    private Integer hasRead;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;
}
