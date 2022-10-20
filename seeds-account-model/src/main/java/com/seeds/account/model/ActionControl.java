package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 操作控制
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
@TableName("ac_action_control")
@ApiModel(value = "ActionControl对象", description = "操作控制")
@Data
@Builder
public class ActionControl implements Serializable {

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
