package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 全局配置
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
@TableName("system_config")
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
    private String type;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("key")
    private String key;

    @ApiModelProperty("value")
    private String value;

    @ApiModelProperty("comments")
    private String comments;

    private Integer status;

}
