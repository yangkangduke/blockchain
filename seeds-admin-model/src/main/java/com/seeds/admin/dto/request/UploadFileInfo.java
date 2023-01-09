package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/1/3
 */
@Data
@ApiModel(value = "UploadFileInfo", description = "文件信息")
public class UploadFileInfo {

    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * @see com.seeds.admin.enums.GameSrcTypeEnum
     */
    private Integer type; // 资源类型

    @ApiModelProperty(value = "文件类型")
    private String contentType;
}
