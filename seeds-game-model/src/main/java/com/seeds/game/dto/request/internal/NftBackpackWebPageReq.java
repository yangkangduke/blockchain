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


    @ApiModelProperty(value = "type  1装备 2道具")
    private Integer type;

    @ApiModelProperty(value = "分类id")
    private Integer itemTypeId;

    @ApiModelProperty(value = "皮肤名字")
    private String skin;

    @ApiModelProperty(value = "英雄类型（职业） 1ASSASSIN, 2TANK, 3ARCHER,4WARRIOR, 5SUPPORT")
    private String heroType;

    @ApiModelProperty(value = "serverRoleId")
    private Long serverRoleId;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序类型 0 asc:升序 1 desc:降序")
    private Integer sortType;

    private String sortTypeStr;

    @ApiModelProperty(value = "state")
    /**
     * @see  com.seeds.game.enums.NFTEnumConstant.NFTStateEnum
     */
    private Integer state;

    public static String convert(Integer sortType) {
        String str = "desc";
        if (sortType.equals(0)) {
            str = "asc";
        }
        return str;
    }
}
