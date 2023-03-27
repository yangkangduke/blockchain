package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: hewei
 * @date 2023/3/23
 */
@Data
@ApiModel(value = "nftEvent")
public class NftEventResp {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("事件类型 1.mint 2.compound 3.other")
    private Integer type;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("1.Pending 2.Cancelled 3.Minted")
    private Integer status;

    @ApiModelProperty("1.Normal 2.Rare 3.Epic")
    private Integer detail;

    private String transferFrom;

    private String transferTo;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("修改人")
    private Long updatedBy;

    @ApiModelProperty("修改时间")
    private Long updatedAt;

    @ApiModelProperty("装备")
    private List<NftEventEquipmentResp> eventEquipments;

}
