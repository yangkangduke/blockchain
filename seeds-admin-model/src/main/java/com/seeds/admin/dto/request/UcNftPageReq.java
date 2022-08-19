package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hang.yu
 * @date 2022/8/19
 */
@Data
@ApiModel(value = "UcNftPageReq", description = "NFT分页请求入参")
public class UcNftPageReq extends PageReq {

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "User id can not empty!")
    private Long userId;

    @ApiModelProperty(value = "游戏id")
    private Long gameId;

    @ApiModelProperty(value = "用户账户id")
    private Long accountId;

}
