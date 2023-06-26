package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 游戏视频tag
 * </p>
 *
 * @author hewei
 * @since 2023-06-25
 */
@Data
@TableName("sys_game_videos_tags")
@ApiModel(value = "SysGameVideosTags对象", description = "游戏视频tag")
public class SysGameVideosTagsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("tag名")
    private String tName;

    @ApiModelProperty("创建者")
    private Long createdBy;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("修改者")
    private Long updatedBy;

    @ApiModelProperty("修改时间")
    private Long updatedAt;
}
