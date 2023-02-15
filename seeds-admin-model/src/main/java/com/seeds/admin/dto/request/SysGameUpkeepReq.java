package com.seeds.admin.dto.request;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
/**
 * 游戏维护中/正常
 *
 * @author dengyang
 * @date 2023/2/15
 */
@Data
@ApiModel(value = "SysGameUpkeepReq", description = "游戏正在维护中状态/游戏状态正常请求入参")
public class SysGameUpkeepReq {

    @ApiModelProperty(value = "编号")
    @NotNull(message = "The id cannot be empty")
    private Long id;

    /**
     * 游戏是否维护中  0：维护中  1：正常
     */
    @ApiModelProperty("游戏是否维护中  0：维护中   1：正常 ")
    private Integer upkeep;
}
