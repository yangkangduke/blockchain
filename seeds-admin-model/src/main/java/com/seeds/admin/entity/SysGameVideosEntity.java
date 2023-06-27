package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 游戏视频管理
 * </p>
 *
 * @author hewei
 * @since 2023-06-25
 */
@Data
@TableName("sys_game_videos")
@ApiModel(value = "SysGameVideos对象", description = "游戏视频管理")
public class SysGameVideosEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("视频标题")
    private String title;

    @ApiModelProperty("视频描述")
    @TableField(value = "`desc`")
    private String desc;

    @ApiModelProperty("原始尺寸高")
    private Integer originalSizeHigh;

    @ApiModelProperty("原始尺寸宽")
    private Integer originalSizeWide;

    @ApiModelProperty("视频URL")
    private String videoUrl;

    @ApiModelProperty("预览图")
    private String previewImg;

    @ApiModelProperty("视频源：1.YouTube 2.instagram")
    private Integer videoSrc;

    @ApiModelProperty("视频标签")
    private String videoTag;

    @ApiModelProperty("视频标签名称")
    private String videoTagName;

    @ApiModelProperty("是否置顶：1是 0 否")
    private Integer isTop;

    @ApiModelProperty("是否上架 1 是 0 否")
    private Integer onShelves;

    @ApiModelProperty("创建者")
    private Long createdBy;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("修改者")
    private Long updatedBy;

    @ApiModelProperty("修改时间")
    private Long updatedAt;
}
