package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: hewei
 * @date 2023/5/11
 */

@Data
@ApiModel(value = "ListStringReq", description = "列表请求入参")
public class ListStringReq {
    @ApiModelProperty(value = "文件名集合")
    @NotEmpty(message = "The fileName list cannot be empty")
    private List<String> fileNames;
}
