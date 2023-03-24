package com.seeds.game.dto.request.internal;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 游戏服角色
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ServerRolePageReq")
@Data
public class ServerRolePageReq extends PageReq {

    @ApiModelProperty(value = "userId")
    private Long userId;
}
