package com.seeds.admin.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hewei
 * @date 2023/4/26
 */
@Data
@ApiModel(value = "SysSkinNftMintReq")
public class SysSkinNftMintReq {

    @ApiModelProperty("勾选条目的id集合")
    private List<Long> ids;
}
