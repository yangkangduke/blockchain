package com.seeds.game.dto.request.internal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: hewei
 * @date 2023/3/23
 */
@Data
@ApiModel(value = "toNFT请求req")
public class NftEventAddReq /*extends OpenSignReq*/ {

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("事件类型 1.mint 2.compound")
    @NotNull
    private Integer type;

    @ApiModelProperty("name")
    @NotBlank
    private String name;

    @ApiModelProperty("1.Normal 2.Rare 3.Epic")
    @NotNull
    private Integer detail;

    @ApiModelProperty("涉及的装备")
    @NotEmpty
    private List<NftEventEquipmentReq> eventEquipments;
}
