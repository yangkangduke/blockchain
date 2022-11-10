package com.seeds.notification.dto.request;

import com.seeds.notification.enums.NoticeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hewei
 * @date 2022/9/21
 */
@Data
@Builder
public class NotificationReq {
    @ApiModelProperty("用户id")
    private List<Long> ucUserIds;

    /**
     * @see NoticeTypeEnum
     */
    @ApiModelProperty("通知类型")
    private Integer notificationType;

    @ApiModelProperty("消息内容")
    private Map<String, Object> values = new HashMap<>();
}
