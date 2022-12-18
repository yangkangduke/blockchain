package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2022/12/17
 */
@Data
public class GameFileResp {

    @ApiModelProperty(value = "文件id")
    private Long fileId;

    @ApiModelProperty(value = "对象名")
    private String objectName;

    @ApiModelProperty(value = "桶")
    private String bucketName;

    @ApiModelProperty(value = "url")
    private String url;
}
