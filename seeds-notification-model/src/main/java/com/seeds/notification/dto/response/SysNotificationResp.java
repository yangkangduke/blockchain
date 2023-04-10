package com.seeds.notification.dto.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class SysNotificationResp implements Serializable {
    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("通知类型 1.announcement  2. notice")
    private Integer type;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("创建人")
    private Long createdAt;

    @ApiModelProperty("创建时间")
    private Long createdBy;

    @ApiModelProperty("修改时间")
    private Long updatedAt;

    @ApiModelProperty("修改人")
    private Long updatedBy;
}
