package com.seeds.notification.dto.request;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2022/9/21
 */
@Data
@ApiModel(value = "通知列表请求入参")
public class SysNoticePageReq extends PageReq {

    @ApiModelProperty(value = "serId")
    private Long userId;

    @ApiModelProperty("通知类型 1.announcement  2. notice")
    private Integer type;
}
