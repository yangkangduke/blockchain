package com.seeds.notification.dto.request;

import com.seeds.common.dto.PageReq;
import com.seeds.common.enums.TargetSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2022/9/21
 */
@Data
@ApiModel(value = "通知列表请求入参")
public class NoticePageReq extends PageReq {

    @ApiModelProperty(value = "ucUserId")
    private Long ucUserId;

    @ApiModelProperty("用户来源, UC: uc端， ADMIN：管理后台，默认为UC")
    private String userSource = TargetSource.UC.name();

}
