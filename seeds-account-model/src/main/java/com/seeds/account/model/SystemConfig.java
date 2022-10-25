package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 全局配置
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
@TableName("ac_system_config")
@ApiModel(value = "SystemConfig对象", description = "全局配置")
@Data
@Builder
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("version")
    private Long version;

    @ApiModelProperty("type")
    @TableField(value = "type")
    private String type;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("key")
    @TableField(value = "key")
    private String key;

    @ApiModelProperty("value")
    @TableField(value = "value")
    private String value;

    @ApiModelProperty("comments")
    private String comments;

    @TableField(value = "status")
    private Integer status;

}
