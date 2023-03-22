package com.seeds.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 系统通知、用户关系表
 * </p>
 *
 * @author hewei
 * @since 2023-03-22
 */
@TableName("sys_notification_user_relation")
@ApiModel(value = "SysNotificationUserRelation对象", description = "系统通知、用户关系表")
public class SysNotificationUserRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("通知id")
    private Long notificationId;
}
