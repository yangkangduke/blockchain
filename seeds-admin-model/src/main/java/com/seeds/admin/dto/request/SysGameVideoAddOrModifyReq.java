package com.seeds.admin.dto.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: he.wei
 * @date 2023/6/25
 */
@Data
public class SysGameVideoAddOrModifyReq {
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty("是否上架：1是 0 否")
    private Integer onShelves;

    @ApiModelProperty("是否置顶：1是 0 否")
    private Integer isTop;
}
