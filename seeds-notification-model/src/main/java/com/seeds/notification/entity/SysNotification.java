package com.seeds.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 系统通知
 * </p>
 *
 * @author hewei
 * @since 2023-03-22
 */
@TableName("sys_notification")
@ApiModel(value = "SysNotification对象", description = "系统通知")
@Data
public class SysNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("通知类型 1.announcement  2. notice")
    private Boolean type;

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
