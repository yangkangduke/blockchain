package com.seeds.game.dto.request.internal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/28
 */

@Data
@ApiModel(value = "NftBackpackWebPageReq")
public class NftBackpackWebPageReq {
    private Long userId;

    @ApiModelProperty(value = "分类id")
    private Integer itemTypeId;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序类型 asc:升序 desc:降序")
    private String sortType;
}
