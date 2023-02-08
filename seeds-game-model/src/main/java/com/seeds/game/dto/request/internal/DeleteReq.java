package com.seeds.game.dto.request.internal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/2/4
 */
@Data
public class DeleteReq {

    @ApiModelProperty(value = "记录ID", required = true)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;
}
