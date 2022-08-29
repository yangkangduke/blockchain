package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hewei
 * @date 2022/8/26
 */
@Data
@ApiModel(value = "SysGameCommentsLikeOrBuryResp", description = "评论赞、踩")
public class SysGameCommentsLikeOrBuryResp implements Serializable {

    @ApiModelProperty(value = "是否点了赞")
    private Integer isLike = 0;
    @ApiModelProperty(value = "点赞数")
    private Integer likeNum = 0;
    @ApiModelProperty(value = "是否踩了")
    private Integer isBury = 0;
    @ApiModelProperty(value = "被踩数")
    private Integer buryNum = 0;
}
