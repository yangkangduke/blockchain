package com.seeds.game.dto.request.internal;

import com.seeds.game.dto.request.OpenSignReq;
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
public class NftEventAddReq extends OpenSignReq {

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty(value = "游戏服务角色id", required = true)
    @NotNull(message = "游戏服角色ID不能为空")
    private Long serverRoleId;

    @ApiModelProperty("事件类型 1.mint 2.compound")
    @NotNull(message = "type can not be null")
    private Integer type;


    @ApiModelProperty("涉及的装备")
    @NotEmpty(message = "equipments can not be null")
    private List<NftEventEquipmentReq> equipments;
}
