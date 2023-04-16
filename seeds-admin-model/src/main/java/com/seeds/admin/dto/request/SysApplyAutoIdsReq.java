package com.seeds.admin.dto.request;

/**
 * @author: hewei
 * @date 2023/4/15
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class SysApplyAutoIdsReq {

    @ApiModelProperty(value = "configId")
    @NotEmpty(message = "The configId list cannot be empty")
    private Set<Long> configIds;
}
