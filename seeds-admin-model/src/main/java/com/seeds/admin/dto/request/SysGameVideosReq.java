package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: he.wei
 * @date 2023/6/25
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "SysGameVideosReq")
public class SysGameVideosReq extends PageReq {

    @ApiModelProperty(value = "视频标题")
    private String title;

    @ApiModelProperty(value = "视频标签  1,Assassin 2,Tank 3,Archer 4,Warrior 5,Support")
    private String tag;

    @ApiModelProperty(value = "是否上架")
    private Integer onShelves;

    @ApiModelProperty(value = "是否置顶")
    private Integer isTop;

}