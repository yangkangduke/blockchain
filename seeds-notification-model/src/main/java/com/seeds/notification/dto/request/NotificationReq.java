package com.seeds.notification.dto.request;

import com.seeds.common.enums.TargetSource;
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

    @ApiModelProperty("通知类型")
    private String notificationType;

    @Builder.Default
    @ApiModelProperty("用户来源, UC: uc端， ADMIN：管理后台，默认为UC")
    private String userSource = TargetSource.UC.name();

    @ApiModelProperty("消息内容")
    private Map<String, Object> values = new HashMap<>();

}
