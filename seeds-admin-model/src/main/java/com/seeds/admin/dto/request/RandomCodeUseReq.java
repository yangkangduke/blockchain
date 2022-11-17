package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "RandomCodeUseReq", description = "使用随机码")
public class RandomCodeUseReq {

    @ApiModelProperty("随机码")
    @NotBlank(message = "Random code cannot be empty")
    private String code;

    @ApiModelProperty(value = "关联用户标识")
    @NotBlank(message = "User identity cannot be empty")
    private String userIdentity;

    @ApiModelProperty("随机码类型 1:邀请码")
    @NotNull(message = "Random code type cannot be empty")
    private Integer type;

}
