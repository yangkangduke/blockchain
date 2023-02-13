package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 游戏服角色
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@TableName("ga_server_role")
@ApiModel(value = "ServerRole对象", description = "游戏服角色")
@Data
public class ServerRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id，游戏那边传的，唯一值")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("UC服务中的用户id")
    private Long userId;

    @ApiModelProperty("大区")
    private Integer region;

    @ApiModelProperty("游戏服")
    private Integer gameServer;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("version")
    private Long version;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("修改人")
    private Long updatedBy;

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;
}