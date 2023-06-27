package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 游戏视频管理
 * </p>
 *
 * @author hewei
 * @since 2023-06-25
 */
@Data
@ApiModel(value = "SysGameVideosResp")
public class SysGameVideosResp implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    @ApiModelProperty("视频标题")
    private String title;

    @ApiModelProperty("视频描述")
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

    @ApiModelProperty("视频标签名称")
    private String videoTagName;

    @ApiModelProperty("视频标签")
    private String videoTag;

    @ApiModelProperty("是否置顶：1是 0 否")
    private Integer isTop;

    @ApiModelProperty("是否删除 1 是 0 否")
    private Integer onShelves;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("修改时间")
    private Long updatedAt;
}
