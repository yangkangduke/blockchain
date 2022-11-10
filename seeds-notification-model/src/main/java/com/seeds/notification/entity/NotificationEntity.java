package com.seeds.notification.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


/**
 * 消息推送记录表
 *
 * @TableName notification
 */
@TableName(value = "notification")
@Data
public class NotificationEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long ucUserId;

    /**
     * 消息类型
     */
    private Integer notificationType;
    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读，0 未读 1 已读
     */
    private Integer hasRead;

    /**
     * 删除标记  1：已删除   0：未删除
     */
    @TableField(value = "delete_flag")
    @TableLogic
    private Integer deleteFlag;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;


    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;
}